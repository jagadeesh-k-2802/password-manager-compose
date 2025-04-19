package com.jackappsdev.password_manager.presentation.screens.settings.event

sealed class SettingsUiEffect {
    data object OpenImportPasswordsIntent : SettingsUiEffect()
    data object OpenExportPasswordsIntent : SettingsUiEffect()
    data object PasswordsExported : SettingsUiEffect()
    data object BiometricAuthenticate : SettingsUiEffect()
    data object OpenScreenLockSettings : SettingsUiEffect()
    data object NavigateToChangePassword : SettingsUiEffect()
    data object NavigateToManageCategories : SettingsUiEffect()
    data object NavigateToAndroidWatch : SettingsUiEffect()
    data object OpenPlayStorePage : SettingsUiEffect()
}
