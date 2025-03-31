package com.jackappsdev.password_manager.presentation.screens.password_lock

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.isScreenLockAvailable
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordLockViewModel @Inject constructor(
    private val application: Application,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(), EventDrivenViewModel<PasswordLockUiEvent, PasswordLockUiEffect> {

    var state by mutableStateOf(PasswordLockState())
        private set

    private val _effectChannel = Channel<PasswordLockUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<PasswordLockError>()
    val errorFlow = _errorChannel.receiveAsFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            state = state.copy(
                hasPasswordSet = userPreferencesRepository.hasPasswordSet(),
                useScreenLockToUnlock = userPreferencesRepository.getScreenLockToUnlock(),
                isScreenLockAvailable = isScreenLockAvailable(application.applicationContext)
            )
        }
    }

    private fun setNewPassword(): PasswordLockUiEffect {
        viewModelScope.launch {
            if (state.password != state.confirmPassword) {
                _errorChannel.send(PasswordLockError.ConfirmPasswordError(R.string.error_passwords_not_match))
            } else if (state.password.isEmpty()) {
                _errorChannel.send(PasswordLockError.PasswordError(R.string.error_password_not_empty))
            } else if (state.confirmPassword.length < 8) {
                _errorChannel.send(PasswordLockError.PasswordError(R.string.error_password_is_small))
            } else if (state.confirmPassword.isEmpty()) {
                _errorChannel.send(PasswordLockError.PasswordError(R.string.error_confirm_password_not_empty))
            } else {
                userPreferencesRepository.setPassword(state.password)
                state = state.copy(hasPasswordSet = true)
                setUnlocked(true)
            }
        }

        return PasswordLockUiEffect.HideKeyboard
    }

    fun setUnlocked(hasBeenUnlocked: Boolean): PasswordLockUiEffect? {
        state = state.copy(
            hasBeenUnlocked = hasBeenUnlocked,
            password = "",
            showPassword = false,
            confirmPassword = "",
            showConfirmPassword = false
        )
        return null
    }

    private suspend fun verifyPassword(): PasswordLockUiEffect? {
        return if (userPreferencesRepository.verifyPassword(state.password)) {
            setUnlocked(true)
            PasswordLockUiEffect.HideKeyboard
        } else {
            _errorChannel.send(PasswordLockError.PasswordError(R.string.error_incorrect_password))
            null
        }
    }

    private fun enterPassword(password: String) {
        state = state.copy(password = password)
    }

    private fun enterConfirmPassword(password: String) {
        state = state.copy(confirmPassword = password)

    }

    private fun toggleShowPassword() {
        state = state.copy(showPassword = !state.showPassword)
    }

    private fun toggleShowConfirmPassword() {
        state = state.copy(showConfirmPassword = !state.showConfirmPassword)
    }

    override fun onEvent(event: PasswordLockUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordLockUiEvent.SetupNewPassword -> setNewPassword()
                is PasswordLockUiEvent.VerifyPassword -> verifyPassword()
                is PasswordLockUiEvent.EnterConfirmPassword -> enterConfirmPassword(event.password)
                is PasswordLockUiEvent.SetUnlocked -> setUnlocked(event.unlocked)
                is PasswordLockUiEvent.EnterPassword -> enterPassword(event.password)
                is PasswordLockUiEvent.ToggleShowPassword -> toggleShowPassword()
                is PasswordLockUiEvent.ToggleShowConfirmPassword -> toggleShowConfirmPassword()
                is PasswordLockUiEvent.BiometricAuthenticate -> PasswordLockUiEffect.BiometricAuthenticate
            }

            if (effect is PasswordLockUiEffect) { _effectChannel.send(effect) }
        }
    }
}
