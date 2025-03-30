package com.jackappsdev.password_manager.presentation.screens.password_lock.event

sealed class PasswordLockUiEvent {
    data class OnNumberPress(val number: String) : PasswordLockUiEvent()
    data object OnBackSpacePress : PasswordLockUiEvent()
    data object VerifyPin : PasswordLockUiEvent()
    data object OpenPhoneApp: PasswordLockUiEvent()
}
