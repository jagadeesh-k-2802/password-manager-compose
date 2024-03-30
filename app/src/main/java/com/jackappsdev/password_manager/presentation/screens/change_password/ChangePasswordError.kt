package com.jackappsdev.password_manager.presentation.screens.change_password

sealed interface ChangePasswordError {
    data class CurrentPasswordError(val error: String) : ChangePasswordError
    data class NewPasswordError(val error: String) : ChangePasswordError
}
