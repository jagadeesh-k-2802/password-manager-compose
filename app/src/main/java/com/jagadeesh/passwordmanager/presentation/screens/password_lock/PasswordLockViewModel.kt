package com.jagadeesh.passwordmanager.presentation.screens.password_lock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
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

    val errorChannel = Channel<String>()

    init {
        getPasswordSet()
    }

    private fun getPasswordSet() {
        viewModelScope.launch {
            state = state.copy(hasPasswordSet = userPreferencesRepository.hasPasswordSet())
        }
    }

    fun setNewPassword(newPassword: String, confirmNewPassword: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (newPassword != confirmNewPassword) {
                errorChannel.send("Passwords do not match")
            } else if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                errorChannel.send("Password should not be empty")
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
            errorChannel.send("Incorrect Password")
            false
        }
    }
}
