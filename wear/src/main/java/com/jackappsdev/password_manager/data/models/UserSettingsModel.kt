package com.jackappsdev.password_manager.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val pin: String? = null
)
