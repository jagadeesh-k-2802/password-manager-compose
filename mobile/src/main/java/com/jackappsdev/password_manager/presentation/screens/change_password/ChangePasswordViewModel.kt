package com.jackappsdev.password_manager.presentation.screens.change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEffect
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val passphraseRepository: PassphraseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(), EventDrivenViewModel<ChangePasswordUiEvent, ChangePasswordUiEffect> {

    var state by mutableStateOf(ChangePasswordState())
        private set

    private val _effectChannel = Channel<ChangePasswordUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<ChangePasswordError>()
    val errorFlow = _errorChannel.receiveAsFlow()

    private fun onPasswordChanged() {
        viewModelScope.launch {
            if (state.currentPassword.isEmpty()) {
                _errorChannel.send(ChangePasswordError.CurrentPasswordError(R.string.error_password_not_empty))
            } else if (state.newPassword.isEmpty()) {
                _errorChannel.send(ChangePasswordError.NewPasswordError(R.string.error_new_password_not_empty))
            } else if (state.newPassword.length < 8) {
                _errorChannel.send(ChangePasswordError.NewPasswordError(R.string.error_password_is_small))
            } else if (state.currentPassword == state.newPassword) {
                _errorChannel.send(ChangePasswordError.NewPasswordError(R.string.error_password_not_same))
            } else if (!userPreferencesRepository.verifyPassword(state.currentPassword)) {
                _errorChannel.send(ChangePasswordError.CurrentPasswordError(R.string.error_wrong_password))
            } else {
                passphraseRepository.updatePassword(state.newPassword)
                _effectChannel.send(ChangePasswordUiEffect.OnPasswordChanged)
            }
        }
    }

    private fun onPasswordEnter(password: String) {
        state = state.copy(currentPassword = password)
    }

    private fun onNewPasswordEnter(password: String) {
        state = state.copy(newPassword = password)
    }

    private fun toggleShowPassword() {
        state = state.copy(showPassword = !state.showPassword)
    }

    private fun toggleShowNewPassword() {
        state = state.copy(showNewPassword = !state.showNewPassword)
    }

    override fun onEvent(event: ChangePasswordUiEvent) {
        viewModelScope.launch {
            when(event) {
                is ChangePasswordUiEvent.OnNewPasswordEnter -> onNewPasswordEnter(event.password)
                is ChangePasswordUiEvent.OnPasswordEnter -> onPasswordEnter(event.password)
                is ChangePasswordUiEvent.ToggleShowNewPassword -> toggleShowPassword()
                is ChangePasswordUiEvent.ToggleShowPassword -> toggleShowNewPassword()
                is ChangePasswordUiEvent.OnPasswordChanged -> onPasswordChanged()
            }
        }
    }
}
