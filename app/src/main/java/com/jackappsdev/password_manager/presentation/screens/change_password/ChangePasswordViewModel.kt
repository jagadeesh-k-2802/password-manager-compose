package com.jackappsdev.password_manager.presentation.screens.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
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
            if (currentPassword.isEmpty()) {
                errorChannel.send(
                    ChangePasswordError.CurrentPasswordError(R.string.error_password_not_empty)
                )
            } else if (newPassword.isEmpty()) {
                errorChannel.send(
                    ChangePasswordError.NewPasswordError(R.string.error_new_password_not_empty)
                )
            } else if (currentPassword == newPassword) {
                errorChannel.send(
                    ChangePasswordError.NewPasswordError(R.string.error_password_not_same)
                )
            } else if (!userPreferencesRepository.verifyPassword(currentPassword)) {
                errorChannel.send(
                    ChangePasswordError.CurrentPasswordError(R.string.error_wrong_password)
                )
            } else {
                passphraseRepository.updatePassword(newPassword)
                onSuccess()
            }
        }
    }
}
