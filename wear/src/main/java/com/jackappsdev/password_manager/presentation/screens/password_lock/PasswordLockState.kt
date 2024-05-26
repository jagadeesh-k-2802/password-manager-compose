package com.jackappsdev.password_manager.presentation.screens.password_lock

data class PasswordLockState(
    val hasPinSet: Boolean? = null,
    val hasBeenUnlocked: Boolean = false
)
