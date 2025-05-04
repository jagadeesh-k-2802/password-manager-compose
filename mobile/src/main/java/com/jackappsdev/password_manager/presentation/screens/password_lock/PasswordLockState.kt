package com.jackappsdev.password_manager.presentation.screens.password_lock

import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class PasswordLockState(
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val password: String = EMPTY_STRING,
    val confirmPassword: String = EMPTY_STRING,
    val hasPasswordSet: Boolean? = null,
    val useScreenLockToUnlock: Boolean? = null,
    val isScreenLockAvailable: Boolean? = null
)
