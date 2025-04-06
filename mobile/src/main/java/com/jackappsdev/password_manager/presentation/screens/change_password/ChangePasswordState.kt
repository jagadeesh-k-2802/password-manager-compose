package com.jackappsdev.password_manager.presentation.screens.change_password

import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class ChangePasswordState(
    val currentPassword: String = EMPTY_STRING,
    val newPassword: String = EMPTY_STRING,
    val showPassword: Boolean = false,
    val showNewPassword: Boolean = false
)
