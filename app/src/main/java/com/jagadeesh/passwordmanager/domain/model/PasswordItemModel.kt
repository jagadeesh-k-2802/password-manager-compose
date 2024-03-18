package com.jagadeesh.passwordmanager.domain.model

data class PasswordItemModel(
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
)
