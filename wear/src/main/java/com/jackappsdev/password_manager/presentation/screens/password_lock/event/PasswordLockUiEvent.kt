package com.jackappsdev.password_manager.presentation.screens.password_lock.event

sealed class PasswordLockUiEvent {
    data class NumberPress(val number: String) : PasswordLockUiEvent()
    data object BackSpacePress : PasswordLockUiEvent()
    data object VerifyPin : PasswordLockUiEvent()
    data object OpenPhoneApp: PasswordLockUiEvent()
}
