package com.jackappsdev.password_manager.presentation.screens.password_lock.event

sealed class PasswordLockUiEvent {
    data class EnterPassword(val password: String) : PasswordLockUiEvent()
    data class EnterConfirmPassword(val password: String) : PasswordLockUiEvent()
    data class SetUnlocked(val unlocked: Boolean) : PasswordLockUiEvent()
    data object ToggleShowPassword : PasswordLockUiEvent()
    data object ToggleShowConfirmPassword : PasswordLockUiEvent()
    data object SetupNewPassword : PasswordLockUiEvent()
    data object VerifyPassword : PasswordLockUiEvent()
    data object BiometricAuthenticate : PasswordLockUiEvent()
}
