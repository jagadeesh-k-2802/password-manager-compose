package com.jackappsdev.password_manager.shared.core

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Generic Jetpack Datastore Serializer for generic types.
 * which provides encryption using [CryptoManager]
 */
class DataStoreEncryptionSerializer<T : @Serializable Any>(
    private val cryptoManager: CryptoManager = CryptoManager(),
    private val serializer: KSerializer<T>,
    override val defaultValue: T
) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(serializer, decryptedBytes.decodeToString())
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(serializer, t).encodeToByteArray(),
            outputStream = output
        )
    }
}
