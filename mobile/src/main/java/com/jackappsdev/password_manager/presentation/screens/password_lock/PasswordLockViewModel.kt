package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordLockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var state by mutableStateOf(PasswordLockState())
        private set

    val errorChannel = Channel<PasswordLockError>()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            state = state.copy(
                hasPasswordSet = userPreferencesRepository.hasPasswordSet(),
                useScreenLockToUnlock = userPreferencesRepository.getScreenLockToUnlock()
            )
        }
    }

    fun setNewPassword(newPassword: String, confirmNewPassword: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (newPassword != confirmNewPassword) {
                errorChannel.send(PasswordLockError.ConfirmPasswordError(R.string.error_passwords_not_match))
            } else if (newPassword.isEmpty()) {
                errorChannel.send(PasswordLockError.PasswordError(R.string.error_password_not_empty))
            } else if (confirmNewPassword.length < 8) {
                errorChannel.send(PasswordLockError.PasswordError(R.string.error_password_is_small))
            } else if (confirmNewPassword.isEmpty()) {
                errorChannel.send(PasswordLockError.PasswordError(R.string.error_confirm_password_not_empty))
            } else {
                userPreferencesRepository.setPassword(newPassword)
                onSuccess()
            }
        }
    }

    fun setUnlocked(hasBeenUnlocked: Boolean) {
        state = state.copy(hasBeenUnlocked = hasBeenUnlocked)
    }

    suspend fun verifyPassword(password: String): Boolean {
        val isMatching = userPreferencesRepository.verifyPassword(password)

        return if (isMatching) {
            true
        } else {
            errorChannel.send(PasswordLockError.PasswordError(R.string.error_incorrect_password))
            false
        }
    }
}
