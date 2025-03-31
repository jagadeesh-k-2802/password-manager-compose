package com.jackappsdev.password_manager.presentation.screens.password_lock

data class PasswordLockState(
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val password: String = "",
    val confirmPassword: String = "",
    val hasPasswordSet: Boolean? = null,
    val hasBeenUnlocked: Boolean = false,
    val useScreenLockToUnlock: Boolean? = null,
    val isScreenLockAvailable: Boolean? = null
)
