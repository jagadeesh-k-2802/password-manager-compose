package com.jackappsdev.password_manager.core

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

const val IMAGES_JPEG_MIME = "image/jpeg"
const val IMAGES_MIME = "image/"
const val PDF_MIME = "application/pdf"
const val MAX_ATTACHMENTS_PER_PASSWORD = 5
const val MAX_IMAGE_SIZE_MB = 2
const val MAX_PDF_SIZE_MB = 5
const val MAX_IMAGE_SIZE_BYTES = MAX_IMAGE_SIZE_MB * 1024 * 1024
const val MAX_PDF_SIZE_BYTES = MAX_PDF_SIZE_MB * 1024 * 1024
const val ALL_SUPPORTED_MIME_TYPES = "*/*"

@Suppress("ArrayInDataClass")
sealed class AttachmentPickResult {
    data class Success(val bytes: ByteArray, val isPdf: Boolean) : AttachmentPickResult()
    data object TooLarge : AttachmentPickResult()
    data object InvalidType : AttachmentPickResult()
    data object ReadFailed : AttachmentPickResult()
}

/**
 * Reads the attachment at [uri] into a [ByteArray] while validating:
 *  - The content resolver reports an Image or PDF MIME type
 *  - The payload is not larger than [maxImageBytes] for images or [maxPdfBytes] for PDFs
 */
fun readAttachment(
    context: Context,
    uri: Uri,
    maxImageBytes: Int,
    maxPdfBytes: Int
): AttachmentPickResult {
    val resolver = context.contentResolver
    val mimeType = resolver.getType(uri)
    
    val isImage = mimeType?.startsWith(IMAGES_MIME) == true
    val isPdf = mimeType == PDF_MIME

    if (!isImage && !isPdf) {
        return AttachmentPickResult.InvalidType
    }

    val maxBytes = if (isPdf) maxPdfBytes else maxImageBytes

    // Fast-path reject using the reported file size when available.
    runCatching {
        resolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (idx != -1 && !cursor.isNull(idx)) {
                    val reportedSize = cursor.getLong(idx)
                    if (reportedSize > maxBytes) return AttachmentPickResult.TooLarge
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
                if (total > maxBytes) { return@runCatching AttachmentPickResult.TooLarge }
                output.write(buffer, 0, read)
            }
            AttachmentPickResult.Success(output.toByteArray(), isPdf)
        } ?: AttachmentPickResult.ReadFailed
    }.getOrElse { AttachmentPickResult.ReadFailed }
}

/**
 * Helper to check if a ByteArray starts with the PDF magic number (%PDF-).
 */
fun ByteArray.isPdf(): Boolean {
    return size >= 5 &&
        this[0] == 0x25.toByte() && // %
        this[1] == 0x50.toByte() && // P
        this[2] == 0x44.toByte() && // D
        this[3] == 0x46.toByte() && // F
        this[4] == 0x2D.toByte()    // -
}
