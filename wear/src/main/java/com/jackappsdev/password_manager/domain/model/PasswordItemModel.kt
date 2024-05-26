package com.jackappsdev.password_manager.domain.model

data class PasswordItemModel(
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val createdAt: Long? = null
)
