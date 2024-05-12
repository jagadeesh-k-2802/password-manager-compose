package com.jackappsdev.password_manager.domain.model

data class PasswordCategoryModel(
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val categoryColor: String? = null,
    val createdAt: Long? = null
)
