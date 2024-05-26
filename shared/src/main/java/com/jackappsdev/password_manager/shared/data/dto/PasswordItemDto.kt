package com.jackappsdev.password_manager.shared.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordItemDto(
    val id: Int = 0,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis()
)
