package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordLockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var state by mutableStateOf(PasswordLockState())
        private set

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            state = state.copy(hasPinSet = userPreferencesRepository.hasPinSet())
        }
    }

    fun setUnlocked(hasBeenUnlocked: Boolean) {
        state = state.copy(hasBeenUnlocked = hasBeenUnlocked)
    }

    suspend fun verifyPin(password: String): Boolean {
        return userPreferencesRepository.verifyPin(password)
    }
}
