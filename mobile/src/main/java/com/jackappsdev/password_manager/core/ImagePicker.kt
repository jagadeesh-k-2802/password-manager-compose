package com.jackappsdev.password_manager.core

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

private const val IMAGES_MIME = "image/"

@Suppress("ArrayInDataClass")
sealed class ImagePickResult {
    data class Success(val bytes: ByteArray) : ImagePickResult()
    data object TooLarge : ImagePickResult()
    data object InvalidType : ImagePickResult()
    data object ReadFailed : ImagePickResult()
}

/**
 * Reads the image at [uri] into a [ByteArray] while validating:
 *  - The content resolver reports an image MIME type
 *  - The payload is not larger than [maxBytes]
 */
fun readImageForAttachment(
    context: Context,
    uri: Uri,
    maxBytes: Int
): ImagePickResult {
    val resolver = context.contentResolver
    val mimeType = resolver.getType(uri)
    if (mimeType == null || !mimeType.startsWith(IMAGES_MIME)) {
        return ImagePickResult.InvalidType
    }

    // Fast-path reject using the reported file size when available.
    runCatching {
        resolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (idx != -1 && !cursor.isNull(idx)) {
                    val reportedSize = cursor.getLong(idx)
                    if (reportedSize > maxBytes) return ImagePickResult.TooLarge
                }
            }
        }
    }

    return runCatching {
        resolver.openInputStream(uri)?.use { input ->
            val buffer = ByteArray(8 * 1024)
            val output = java.io.ByteArrayOutputStream()
            var total = 0
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                total += read
                if (total > maxBytes) {
                    return@runCatching ImagePickResult.TooLarge
                }
                output.write(buffer, 0, read)
            }
            ImagePickResult.Success(output.toByteArray())
        } ?: ImagePickResult.ReadFailed
    }.getOrElse { ImagePickResult.ReadFailed }
}
