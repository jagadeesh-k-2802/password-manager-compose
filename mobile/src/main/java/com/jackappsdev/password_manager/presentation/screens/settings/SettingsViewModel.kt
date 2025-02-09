package com.jackappsdev.password_manager.presentation.screens.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.DatabaseManagerRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseManagerRepository: DatabaseManagerRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    init {
        getInitialData()
    }

    private fun getInitialData() {
        viewModelScope.launch {
            state = state.copy(
                useScreenLockToUnlock = userPreferencesRepository.getScreenLockToUnlock(),
                useDynamicColors = userPreferencesRepository.getUseDynamicColors().first()
            )
        }
    }

    fun toggleBiometricUnlock() {
        viewModelScope.launch {
            val toggleValue = state.useScreenLockToUnlock != true
            state = state.copy(useScreenLockToUnlock = toggleValue)
            userPreferencesRepository.setUseScreenLockToUnlock(toggleValue)
        }
    }

    fun setDynamicColors(newValue: Boolean) {
        viewModelScope.launch {
            state = state.copy(useDynamicColors = newValue)
            userPreferencesRepository.setUseDynamicColors(newValue)
        }
    }

    fun importData(uri: Uri, password: String, onFinish: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isDone = databaseManagerRepository.importData(uri.toString(), password)
            onFinish(isDone)
        }
    }

    fun exportData(uri: Uri, onSuccess: () -> Unit) {
        viewModelScope.launch {
            databaseManagerRepository.exportData(uri.toString())
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }
}
