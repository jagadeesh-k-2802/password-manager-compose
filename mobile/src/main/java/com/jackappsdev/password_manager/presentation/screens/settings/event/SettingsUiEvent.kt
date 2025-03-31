package com.jackappsdev.password_manager.presentation.screens.settings.event

sealed class SettingsUiEvent {
    data class ImportPasswords(val password: String) : SettingsUiEvent()
    data class SavePasswordsUri(val uri: String) : SettingsUiEvent()
    data class ExportPasswords(val uri: String) : SettingsUiEvent()
    data object HideImportPasswordsDialog : SettingsUiEvent()
    data object ToggleDynamicColors : SettingsUiEvent()
    data object ToggleUseScreenLock : SettingsUiEvent()
    data object CheckScreenLockAvailable : SettingsUiEvent()
    data object OnImportPasswords : SettingsUiEvent()
    data object OnExportPasswords : SettingsUiEvent()
    data object OpenScreenLockSettings : SettingsUiEvent()
    data object OpenPlayStorePage : SettingsUiEvent()
}
