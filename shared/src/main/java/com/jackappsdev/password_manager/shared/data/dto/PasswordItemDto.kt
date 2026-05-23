package com.jackappsdev.password_manager.shared.data.dto

import com.jackappsdev.password_manager.shared.constants.ZERO
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Data transfer object for password items between Mobile and Wear OS
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class PasswordItemDto(
    val id: Int = ZERO,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis(),
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val timestamp: String? = null
)
