package com.jackappsdev.password_manager.presentation.screens.password_lock

import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class PasswordLockState(
    val pin: String = EMPTY_STRING,
    val hasPinSet: Boolean? = null,
    val hasBeenUnlocked: Boolean = false
)
