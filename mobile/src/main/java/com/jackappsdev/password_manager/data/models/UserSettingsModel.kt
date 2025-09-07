package com.jackappsdev.password_manager.data.models

import com.jackappsdev.password_manager.constants.DEFAULT_APP_AUTO_LOCK_DELAY
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val password: String? = null,
    val pin: String? = null,
    val androidWatchPin: String? = null,
    val useDynamicColors: Boolean = true,
    val useScreenLockToUnlock: Boolean = true,
    val autoLockDelayMs: Long = DEFAULT_APP_AUTO_LOCK_DELAY,
)
