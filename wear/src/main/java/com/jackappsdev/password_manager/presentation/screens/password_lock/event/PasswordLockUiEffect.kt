package com.jackappsdev.password_manager.presentation.screens.password_lock.event

sealed class PasswordLockUiEffect {
    data object Unlock : PasswordLockUiEffect()
    data object IncorrectPassword : PasswordLockUiEffect()
    data object OpenPhoneApp : PasswordLockUiEffect()
}
