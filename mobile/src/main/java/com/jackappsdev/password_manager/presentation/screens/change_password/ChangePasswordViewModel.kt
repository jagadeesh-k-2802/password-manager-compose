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

    private fun onEnterText(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.PasswordEnter -> {
                state = state.copy(currentPassword = event.password)
            }

            is ChangePasswordUiEvent.NewPasswordEnter -> {
                state = state.copy(newPassword = event.password)
            }

            else -> {
                null
            }
        }
    }

    private fun toggleVisibility(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.ToggleShowPasswordVisibility -> {
                state = state.copy(showPassword = !state.showPassword)
            }

            is ChangePasswordUiEvent.ToggleShowNewPasswordVisibility -> {
                state = state.copy(showNewPassword = !state.showNewPassword)
            }

            else -> {
                null
            }
        }
    }

    private suspend fun onPasswordChanged(): ChangePasswordUiEffect? {
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
            return ChangePasswordUiEffect.PasswordUpdated
        }

        return null
    }

    override fun onEvent(event: ChangePasswordUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is ChangePasswordUiEvent.PasswordEnter -> onEnterText(event)
                is ChangePasswordUiEvent.NewPasswordEnter -> onEnterText(event)
                is ChangePasswordUiEvent.ToggleShowPasswordVisibility -> toggleVisibility(event)
                is ChangePasswordUiEvent.ToggleShowNewPasswordVisibility -> toggleVisibility(event)
                is ChangePasswordUiEvent.UpdatePassword -> onPasswordChanged()
                is ChangePasswordUiEvent.NavigateUp -> ChangePasswordUiEffect.NavigateUp
            }

            if (effect is ChangePasswordUiEffect) _effectChannel.send(effect)
        }
    }
}
