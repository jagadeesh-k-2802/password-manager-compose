package com.jackappsdev.password_manager.presentation.screens.settings.event

sealed class SettingsUiEffect {
    data object OnImportPasswords : SettingsUiEffect()
    data object OnExportPasswords : SettingsUiEffect()
    data object BiometricAuthenticate : SettingsUiEffect()
    data object OpenScreenLockSettings : SettingsUiEffect()
    data object OnPasswordsExported : SettingsUiEffect()
    data object OpenPlayStorePage : SettingsUiEffect()
}
