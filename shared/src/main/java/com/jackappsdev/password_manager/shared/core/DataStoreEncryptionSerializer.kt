package com.jackappsdev.password_manager.shared.core

import android.app.ActivityManager
import android.content.Context
import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.BadPaddingException

/**
 * Generic Jetpack Datastore Serializer for generic types.
 * which provides encryption using [CryptoManager]
 */
class DataStoreEncryptionSerializer<T : @Serializable Any>(
    private val context: Context,
    private val serializer: KSerializer<T>,
    override val defaultValue: T
) : Serializer<T> {

    private val cryptoManager: CryptoManager = CryptoManager()

    override suspend fun readFrom(input: InputStream): T {
        return try {
            val decryptedBytes = cryptoManager.decrypt(input)
            Json.decodeFromString(serializer, decryptedBytes.decodeToString())
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        } catch (_: BadPaddingException) {
            // Encryption key has changed should clear the app data
            // Happens for fresh install on same device or different device, so clearing data
            // Also disabled auto backup which causes this issue
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            manager?.clearApplicationUserData()
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
