package com.jackappsdev.password_manager.presentation.screens.settings.model

sealed class ExportPasswordAuthType {
    data object BiometricAuth : ExportPasswordAuthType()
    data class PasswordAuth(val password: String) : ExportPasswordAuthType()
}
