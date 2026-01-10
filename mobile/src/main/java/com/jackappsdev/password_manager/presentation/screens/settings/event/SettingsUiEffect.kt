package com.jackappsdev.password_manager.presentation.screens.settings.event

import com.google.android.play.core.appupdate.AppUpdateManager

sealed class SettingsUiEffect {
    data class StartAppUpdate(val appUpdateManager: AppUpdateManager) : SettingsUiEffect()
    data object OpenImportPasswordsIntent : SettingsUiEffect()
    data object OpenImportChromePasswordsIntent : SettingsUiEffect()
    data object OpenExportPasswordsIntent : SettingsUiEffect()
    data object OpenExportChromePasswordsIntent : SettingsUiEffect()
    data object OpenExportCsvIntent : SettingsUiEffect()
    data object BiometricAuthForExportPasswords : SettingsUiEffect()
    data object BiometricAuthForExportChromePasswords : SettingsUiEffect()
    data object BiometricAuthForExportCsv : SettingsUiEffect()
    data object PasswordsImported : SettingsUiEffect()
    data object PasswordsExported : SettingsUiEffect()
    data object CannotImportChromePasswords : SettingsUiEffect()
    data object BiometricAuthForScreenLock : SettingsUiEffect()
    data object OpenScreenLockSettings : SettingsUiEffect()
    data object NavigateToChangePassword : SettingsUiEffect()
    data object NavigateToManageCategories : SettingsUiEffect()
    data object NavigateToAndroidWatch : SettingsUiEffect()
    data object NavigateToPin : SettingsUiEffect()
    data object OpenAutofillSettings : SettingsUiEffect()
    data object OpenPlayStorePage : SettingsUiEffect()
    data object OpenDonateWithPaypal : SettingsUiEffect()
}
