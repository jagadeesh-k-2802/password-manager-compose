package com.jagadeesh.passwordmanager.presentation.screens.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.repository.DatabaseManagerRepository
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                useScreenLockToUnlock = userPreferencesRepository.getScreenLockToUnlock()
            )
        }
    }

    fun setBiometricUnlock(newValue: Boolean) {
        viewModelScope.launch {
            state = state.copy(useScreenLockToUnlock = newValue)
            userPreferencesRepository.setUseScreenLockToUnlock(newValue)
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
