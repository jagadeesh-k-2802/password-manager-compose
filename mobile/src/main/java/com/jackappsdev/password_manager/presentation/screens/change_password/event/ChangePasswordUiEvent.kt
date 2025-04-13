package com.jackappsdev.password_manager.presentation.screens.change_password.event

sealed class ChangePasswordUiEvent {
    data class OnPasswordEnter(val password: String) : ChangePasswordUiEvent()
    data class OnNewPasswordEnter(val password: String) : ChangePasswordUiEvent()
    data object ToggleShowPassword : ChangePasswordUiEvent()
    data object ToggleShowNewPassword : ChangePasswordUiEvent()
    data object OnPasswordChanged : ChangePasswordUiEvent()
    data object NavigateUp : ChangePasswordUiEvent()
}
