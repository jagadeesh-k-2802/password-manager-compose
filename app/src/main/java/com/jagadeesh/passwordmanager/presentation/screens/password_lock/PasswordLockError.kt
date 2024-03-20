package com.jagadeesh.passwordmanager.presentation.screens.password_lock

sealed interface PasswordLockError {
    data class PasswordError(val error: String) : PasswordLockError
    data class ConfirmPasswordError(val error: String) : PasswordLockError
}
