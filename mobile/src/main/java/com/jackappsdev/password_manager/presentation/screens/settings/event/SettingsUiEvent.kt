package com.jackappsdev.password_manager.presentation.screens.settings.event

import com.jackappsdev.password_manager.presentation.screens.settings.model.ExportPasswordAuthType

sealed class SettingsUiEvent {
    data class ImportPasswords(val password: String) : SettingsUiEvent()
    data class SavePasswordsUri(val uri: String) : SettingsUiEvent()
    data class ExportPasswords(val uri: String) : SettingsUiEvent()
    data class ExportPasswordsCsv(val uri: String) : SettingsUiEvent()
    data object HideImportPasswordsDialog : SettingsUiEvent()
    data object HideExportPasswordsDialog : SettingsUiEvent()
    data object HideExportCsvDialog : SettingsUiEvent()
    data object ToggleDynamicColors : SettingsUiEvent()
    data object ToggleUseScreenLock : SettingsUiEvent()
    data object CompleteAppUpdate : SettingsUiEvent()
    data object CheckScreenLockAvailable : SettingsUiEvent()
    data object CheckExportPasswordsAuth : SettingsUiEvent()
    data object CheckExportCsvAuth : SettingsUiEvent()
    data class OpenExportPasswordsIntent(val passwordType: ExportPasswordAuthType) : SettingsUiEvent()
    data class OpenExportCsvIntent(val passwordType: ExportPasswordAuthType) : SettingsUiEvent()
    data object StartAppUpdate : SettingsUiEvent()
    data object OpenImportPasswordsIntent : SettingsUiEvent()
    data object OpenScreenLockSettings : SettingsUiEvent()
    data object NavigateToChangePassword : SettingsUiEvent()
    data object NavigateToManageCategories : SettingsUiEvent()
    data object NavigateToAndroidWatch : SettingsUiEvent()
    data object NavigateToPin : SettingsUiEvent()
    data object OpenPlayStorePage : SettingsUiEvent()
}
