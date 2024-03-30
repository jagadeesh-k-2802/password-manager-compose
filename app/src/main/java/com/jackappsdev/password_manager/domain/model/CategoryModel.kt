package com.jackappsdev.password_manager.domain.model

data class CategoryModel(
    val id: Int? = null,
    val name: String,
    val color: String,
    val createdAt: Long? = null
)
