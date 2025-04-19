package com.jackappsdev.password_manager.presentation.screens.change_password.event

sealed class ChangePasswordUiEvent {
    data class PasswordEnter(val password: String) : ChangePasswordUiEvent()
    data class NewPasswordEnter(val password: String) : ChangePasswordUiEvent()
    data object ToggleShowPasswordVisibility : ChangePasswordUiEvent()
    data object ToggleShowNewPasswordVisibility : ChangePasswordUiEvent()
    data object UpdatePassword : ChangePasswordUiEvent()
    data object NavigateUp : ChangePasswordUiEvent()
}
