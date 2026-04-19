package com.jackappsdev.password_manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jackappsdev.password_manager.shared.constants.ZERO

@Entity(tableName = "password_items")
data class PasswordItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = ZERO,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "website") val website: String,
    @ColumnInfo(name = "is_added_to_watch") val isAddedToWatch: Boolean,
    @ColumnInfo(name = "category_id") val categoryId: Int? = null,
    @ColumnInfo(name = "images", typeAffinity = ColumnInfo.BLOB) val images: List<ByteArray> = emptyList(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PasswordItemEntity) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (notes != other.notes) return false
        if (website != other.website) return false
        if (isAddedToWatch != other.isAddedToWatch) return false
        if (categoryId != other.categoryId) return false
        if (createdAt != other.createdAt) return false
        if (images.size != other.images.size) return false
        for (i in images.indices) { if (!images[i].contentEquals(other.images[i])) return false }
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + website.hashCode()
        result = 31 * result + isAddedToWatch.hashCode()
        result = 31 * result + (categoryId ?: 0)
        result = 31 * result + images.sumOf { it.contentHashCode() }
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
