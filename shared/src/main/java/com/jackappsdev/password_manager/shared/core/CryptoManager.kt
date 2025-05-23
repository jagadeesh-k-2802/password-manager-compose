package com.jackappsdev.password_manager.shared.core

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * Manages encryption and decryption of data using Android KeyStore encryption.
 * Meant to be used with [DataStoreEncryptionSerializer] for encrypting data in Jetpack DataStore.
 */
internal class CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher
        get() = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setKeySize(KEY_SIZE_BYTES * 8) // key size in bits
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream) {
        val cipher = encryptCipher
        val iv = cipher.iv

        outputStream.use {
            it.write(iv)
            val inputStream = ByteArrayInputStream(bytes)
            val buffer = ByteArray(CHUNK_SIZE_BYTES)

            while (inputStream.available() > CHUNK_SIZE_BYTES) {
                inputStream.read(buffer)
                val ciphertextChunk = cipher.update(buffer)
                it.write(ciphertextChunk)
            }

            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes)
            it.write(lastChunk)
        }
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val iv = ByteArray(KEY_SIZE_BYTES)
            it.read(iv)
            val cipher = getDecryptCipherForIv(iv)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(CHUNK_SIZE_BYTES)

            while (inputStream.available() > CHUNK_SIZE_BYTES) {
                inputStream.read(buffer)
                val ciphertextChunk = cipher.update(buffer)
                outputStream.write(ciphertextChunk)
            }

            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes)
            outputStream.write(lastChunk)
            outputStream.toByteArray()
        }
    }

    companion object {
        private const val CHUNK_SIZE_BYTES = 1024 * 4
        private const val KEY_SIZE_BYTES = 16
        private const val ALIAS = "my_alias"
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
