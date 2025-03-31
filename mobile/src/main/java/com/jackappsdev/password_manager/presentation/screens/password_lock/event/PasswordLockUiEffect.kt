package com.jackappsdev.password_manager.presentation.screens.password_lock.event

sealed class PasswordLockUiEffect {
    data object HideKeyboard : PasswordLockUiEffect()
    data object BiometricAuthenticate : PasswordLockUiEffect()
}
