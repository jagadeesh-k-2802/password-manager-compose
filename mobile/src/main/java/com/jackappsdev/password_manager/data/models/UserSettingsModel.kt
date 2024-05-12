package com.jackappsdev.password_manager.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val password: String? = null,
    val useDynamicColors: Boolean = true,
    val useScreenLockToUnlock: Boolean = true
)
