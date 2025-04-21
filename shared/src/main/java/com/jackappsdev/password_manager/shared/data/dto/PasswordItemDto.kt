package com.jackappsdev.password_manager.shared.data.dto

import com.jackappsdev.password_manager.shared.constants.ZERO
import kotlinx.serialization.Serializable

/**
 * Data transfer object for password items between Mobile and Wear OS
 */
@Serializable
data class PasswordItemDto(
    val id: Int = ZERO,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis()
)
