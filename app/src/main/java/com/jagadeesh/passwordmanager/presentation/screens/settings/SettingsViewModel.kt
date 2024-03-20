package com.jagadeesh.passwordmanager.presentation.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    init {
        getInitialData()
    }

    private fun getInitialData() {
        viewModelScope.launch {
            state = state.copy(
                useBiometricUnlock = userPreferencesRepository.getBiometricUnlock()
            )
        }
    }

    fun setBiometricUnlock(newValue: Boolean) {
        viewModelScope.launch {
            state = state.copy(useBiometricUnlock = newValue)
            userPreferencesRepository.setBiometricUnlock(newValue)
        }
    }
}
