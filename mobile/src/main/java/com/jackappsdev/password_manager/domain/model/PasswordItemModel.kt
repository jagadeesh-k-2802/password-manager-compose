package com.jackappsdev.password_manager.domain.model

import com.jackappsdev.password_manager.shared.constants.ZERO

data class PasswordItemModel(
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val website: String,
    val isAddedToWatch: Boolean,
    val categoryId: Int? = null,
    val images: List<ByteArray> = emptyList(),
    val createdAt: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PasswordItemModel) return false
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
        var result = id ?: ZERO
        result = 31 * result + name.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + website.hashCode()
        result = 31 * result + isAddedToWatch.hashCode()
        result = 31 * result + (categoryId ?: ZERO)
        result = 31 * result + images.sumOf { it.contentHashCode() }
        result = 31 * result + (createdAt?.hashCode() ?: ZERO)
        return result
    }
}
