package com.jackappsdev.password_manager.presentation.screens.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.requestCompleteUpdate
import com.google.android.play.core.ktx.requestUpdateFlow
import com.jackappsdev.password_manager.core.isScreenLockAvailable
import com.jackappsdev.password_manager.domain.repository.DatabaseManagerRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsUiEffect
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseManagerRepository: DatabaseManagerRepository,
    private val appUpdateManager: AppUpdateManager
) : ViewModel(), EventDrivenViewModel<SettingsUiEvent, SettingsUiEffect> {

    var state by mutableStateOf(SettingsState())
        private set

    private val _effectChannel = Channel<SettingsUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            fetchInitialData()
            checkForAppUpdate()
        }
    }

    private suspend fun fetchInitialData() {
        state = state.copy(
            useScreenLockToUnlock = userPreferencesRepository.getScreenLockToUnlock(),
            useDynamicColors = userPreferencesRepository.getUseDynamicColors().first(),
            isScreenLockAvailable = isScreenLockAvailable(application.applicationContext)
        )
    }

    private suspend fun checkForAppUpdate() {
        try {
            appUpdateManager.requestUpdateFlow().collectLatest { appUpdateResult ->
                when (appUpdateResult) {
                    is AppUpdateResult.Available -> {
                        if (appUpdateResult.updateInfo.isFlexibleUpdateAllowed) {
                            state = state.copy(isAppUpdateAvailable = true)
                        }
                    }

                    is AppUpdateResult.InProgress -> {
                        state = state.copy(isAppUpdateDownloading = true)
                    }

                    is AppUpdateResult.Downloaded -> {
                        state = state.copy(isAppUpdateDownloaded = true)
                    }

                    is AppUpdateResult.NotAvailable -> {
                        // No Operation
                    }
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun hideImportPasswordDialog() {
        state = state.copy(isImportPasswordsDialogVisible = false, isImportPasswordInvalid = false)
    }

    private fun checkIfScreenLockAvailable(): SettingsUiEffect {
        return if (state.isScreenLockAvailable == true) {
            SettingsUiEffect.BiometricAuthenticate
        } else {
            SettingsUiEffect.OpenScreenLockSettings
        }
    }

    private suspend fun toggleScreenLock() {
        val toggleValue = state.useScreenLockToUnlock != true
        state = state.copy(useScreenLockToUnlock = toggleValue)
        userPreferencesRepository.setUseScreenLockToUnlock(toggleValue)
    }

    private suspend fun completeAppUpdate() {
        appUpdateManager.requestCompleteUpdate()
    }

    private suspend fun setDynamicColors() {
        val toggleValue = state.useDynamicColors != true
        state = state.copy(useDynamicColors = toggleValue)
        userPreferencesRepository.setUseDynamicColors(toggleValue)
    }

    private fun savePasswordsUri(uri: String) {
        state = state.copy(
            isImportPasswordsDialogVisible = true,
            isImportPasswordInvalid = false,
            importFileUri = uri
        )
    }

    private suspend fun importData(password: String) {
        if (state.importFileUri != null) {
            val uri = state.importFileUri ?: EMPTY_STRING
            val isDone = databaseManagerRepository.importDatabase(uri, password)
            state = state.copy(isImportPasswordInvalid = !isDone)
        }
    }

    private suspend fun exportData(uri: String): SettingsUiEffect {
        databaseManagerRepository.exportDatabase(uri)
        return SettingsUiEffect.PasswordsExported
    }

    override fun onEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is SettingsUiEvent.ImportPasswords -> importData(event.password)
                is SettingsUiEvent.SavePasswordsUri -> savePasswordsUri(event.uri)
                is SettingsUiEvent.ExportPasswords -> exportData(event.uri)
                is SettingsUiEvent.HideImportPasswordsDialog -> hideImportPasswordDialog()
                is SettingsUiEvent.ToggleDynamicColors -> setDynamicColors()
                is SettingsUiEvent.ToggleUseScreenLock -> toggleScreenLock()
                is SettingsUiEvent.CompleteAppUpdate -> completeAppUpdate()
                is SettingsUiEvent.StartAppUpdate -> SettingsUiEffect.StartAppUpdate(appUpdateManager)
                is SettingsUiEvent.CheckScreenLockAvailable -> checkIfScreenLockAvailable()
                is SettingsUiEvent.OpenImportPasswordsIntent -> SettingsUiEffect.OpenImportPasswordsIntent
                is SettingsUiEvent.OpenExportPasswordsIntent -> SettingsUiEffect.OpenExportPasswordsIntent
                is SettingsUiEvent.OpenScreenLockSettings -> SettingsUiEffect.OpenScreenLockSettings
                is SettingsUiEvent.NavigateToChangePassword -> SettingsUiEffect.NavigateToChangePassword
                is SettingsUiEvent.NavigateToManageCategories -> SettingsUiEffect.NavigateToManageCategories
                is SettingsUiEvent.NavigateToAndroidWatch -> SettingsUiEffect.NavigateToAndroidWatch
                is SettingsUiEvent.OpenPlayStorePage -> SettingsUiEffect.OpenPlayStorePage
            }

            if (effect is SettingsUiEffect) { _effectChannel.send(effect) }
        }
    }
}
