package com.jackappsdev.password_manager.presentation.screens.password_lock

data class PasswordLockState(
    val hasPasswordSet: Boolean? = null,
    val hasBeenUnlocked: Boolean = false,
    val useScreenLockToUnlock: Boolean? = null
)
