package com.jackappsdev.password_manager.presentation.screens.settings

data class SettingsState(
    val useScreenLockToUnlock: Boolean? = null,
    val useDynamicColors: Boolean? = null,
    val isImportPasswordsDialogVisible: Boolean = false,
    val isImportPasswordInvalid: Boolean = false,
    val isScreenLockAvailable: Boolean? = null,
    val importFileUri: String? = null
)
