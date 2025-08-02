package com.jackappsdev.password_manager.presentation.screens.settings

data class SettingsState(
    val isAppUpdateAvailable: Boolean? = null,
    val isAppUpdateDownloading: Boolean = false,
    val isAppUpdateDownloaded: Boolean = false,
    val useScreenLockToUnlock: Boolean? = null,
    val useDynamicColors: Boolean? = null,
    val isScreenLockAvailable: Boolean? = null,
    val importFileUri: String? = null,
    val isChromeOS: Boolean = false,
    val isImportPasswordsDialogVisible: Boolean = false,
    val isImportPasswordInvalid: Boolean = false,
    val isImportChromePasswordsDialogVisible: Boolean = false,
    val isExportCsvDialogVisible: Boolean = false,
    val isExportCsvPasswordInvalid: Boolean = false,
    val isExportPasswordsPasswordInvalid: Boolean = false,
    val isExportPasswordsDialogVisible: Boolean = false,
    val isExportChromePasswordsDialogVisible: Boolean = false,
    val isExportChromePasswordsPasswordInvalid: Boolean = false
)
