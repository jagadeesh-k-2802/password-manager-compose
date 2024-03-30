package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                errorChannel.send(PasswordLockError.ConfirmPasswordError("Passwords Don't Match"))
            } else if (newPassword.isEmpty()) {
                errorChannel.send(PasswordLockError.PasswordError("Password Should Not be Empty"))
            } else if (confirmNewPassword.isEmpty()) {
                errorChannel.send(PasswordLockError.PasswordError("Confirm Password Should Not be Empty"))
            } else {
                userPreferencesRepository.setPassword(newPassword)
                onSuccess()
            }
        }
    }

    suspend fun verifyPassword(password: String): Boolean {
        val isMatching = userPreferencesRepository.verifyPassword(password)

        return if (isMatching) {
            true
        } else {
            errorChannel.send(PasswordLockError.PasswordError("Incorrect Password"))
            false
        }
    }
}
