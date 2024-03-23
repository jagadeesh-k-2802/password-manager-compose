package com.jagadeesh.passwordmanager.domain.model

data class PasswordItemModel(
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val categoryId: Int? = null,
    val createdAt: Long? = null
)
