package com.jackappsdev.password_manager.presentation.model

/**
 * Represents an attachment that is ready to be exported, containing the file's byte data, name, and MIME type.
 *
 * @property bytes The byte array representing the content of the attachment.
 * @property fileName The name of the file, including its extension.
 * @property mimeType The MIME type of the file (e.g., "image/png", "application/pdf").
 */
data class AttachmentToExport(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AttachmentToExport) return false
        if (!bytes.contentEquals(other.bytes)) return false
        if (fileName != other.fileName) return false
        if (mimeType != other.mimeType) return false
        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}
