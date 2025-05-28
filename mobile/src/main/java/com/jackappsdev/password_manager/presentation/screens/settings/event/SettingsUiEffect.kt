package com.jackappsdev.password_manager.presentation.screens.settings.event

import com.google.android.play.core.appupdate.AppUpdateManager

sealed class SettingsUiEffect {
    data class StartAppUpdate(val appUpdateManager: AppUpdateManager) : SettingsUiEffect()
    data object OpenImportPasswordsIntent : SettingsUiEffect()
    data object OpenExportPasswordsIntent : SettingsUiEffect()
    data object OpenExportCsvIntent : SettingsUiEffect()
    data object OpenScreenLockSettings : SettingsUiEffect()
    data object NavigateToChangePassword : SettingsUiEffect()
    data object NavigateToManageCategories : SettingsUiEffect()
    data object NavigateToAndroidWatch : SettingsUiEffect()
    data object PasswordsExported : SettingsUiEffect()
    data object BiometricAuthForScreenLock : SettingsUiEffect()
    data object OpenPlayStorePage : SettingsUiEffect()
    data object BiometricAuthForExportCsv : SettingsUiEffect()
}
