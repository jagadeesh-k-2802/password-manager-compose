package com.jackappsdev.password_manager.presentation.screens.change_password.event

sealed class ChangePasswordUiEffect {
    data object PasswordUpdated : ChangePasswordUiEffect()
    data object NavigateUp : ChangePasswordUiEffect()
}
