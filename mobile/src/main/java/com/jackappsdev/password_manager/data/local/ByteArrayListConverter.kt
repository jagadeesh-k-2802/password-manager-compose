package com.jackappsdev.password_manager.data.local

import androidx.room.TypeConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Serializes / deserializes a list of byte arrays into a single BLOB.
 *
 * Format: int(count) followed by [int(size) + bytes] entries.
 */
class ByteArrayListConverter {

    @TypeConverter
    fun fromImages(images: List<ByteArray>): ByteArray {
        val output = ByteArrayOutputStream()
        DataOutputStream(output).use { dos ->
            dos.writeInt(images.size)
            for (bytes in images) {
                dos.writeInt(bytes.size)
                dos.write(bytes)
            }
        }
        return output.toByteArray()
    }

    @TypeConverter
    fun toImages(data: ByteArray?): List<ByteArray> {
        if (data == null || data.isEmpty()) return emptyList()
        return DataInputStream(ByteArrayInputStream(data)).use { dis ->
            val count = runCatching { dis.readInt() }.getOrElse { return emptyList() }
            if (count <= 0) return emptyList()
            val list = ArrayList<ByteArray>(count)
            repeat(count) {
                val size = dis.readInt()
                val bytes = ByteArray(size)
                dis.readFully(bytes)
                list.add(bytes)
            }
            list
        }
    }
}
