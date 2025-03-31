package com.jackappsdev.password_manager.domain.model

data class PasswordWithCategoryModel(
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val website: String,
    val isAddedToWatch: Boolean,
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val categoryColor: String? = null,
    val createdAt: Long? = null
)
