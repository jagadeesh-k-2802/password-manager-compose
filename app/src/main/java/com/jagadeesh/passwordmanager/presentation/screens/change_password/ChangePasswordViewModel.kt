package com.jagadeesh.passwordmanager.presentation.screens.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.repository.PassphraseRepository
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val passphraseRepository: PassphraseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val errorChannel = Channel<ChangePasswordError>()

    fun updatePassword(currentPassword: String, newPassword: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (!userPreferencesRepository.verifyPassword(currentPassword)) {
                errorChannel.send(
                    ChangePasswordError.CurrentPasswordError("Invalid Password")
                )
            } else if (currentPassword.isEmpty()) {
                errorChannel.send(
                    ChangePasswordError.CurrentPasswordError("Current Password should not be empty")
                )
            } else if (newPassword.isEmpty()) {
                errorChannel.send(
                    ChangePasswordError.NewPasswordError("New Password should not be empty")
                )
            } else {
                passphraseRepository.updatePassword(newPassword)
                onSuccess()
            }
        }
    }
}
