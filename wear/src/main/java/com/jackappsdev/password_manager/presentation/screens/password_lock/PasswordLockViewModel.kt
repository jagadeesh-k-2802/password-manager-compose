package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordLockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(), EventDrivenViewModel<PasswordLockUiEvent, PasswordLockUiEffect> {

    var state by mutableStateOf(PasswordLockState())
        private set

    private val _effectChannel = Channel<PasswordLockUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    companion object {
        const val MAX_PIN_COUNT = 4
    }

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            state = state.copy(hasPinSet = userPreferencesRepository.hasPinSet())
        }
    }

    fun lockApp() {
        state = state.copy(hasBeenUnlocked = false, pin = EMPTY_STRING)
    }

    private fun onNumberPress(number: String) {
        if (state.pin.length >= MAX_PIN_COUNT) return
        state = state.copy(pin = state.pin + number)
    }

    private fun onBackSpacePress() {
        if (state.pin.isNotEmpty()) {
            state = state.copy(pin = state.pin.dropLast(1))
        }
    }

    private suspend fun verifyPin(): PasswordLockUiEffect {
        return if (userPreferencesRepository.verifyPin(state.pin)) {
            state = state.copy(hasBeenUnlocked = true, pin = EMPTY_STRING)
            PasswordLockUiEffect.Unlock
        } else {
            state = state.copy(pin = EMPTY_STRING)
            PasswordLockUiEffect.IncorrectPassword
        }
    }

    override fun onEvent(event: PasswordLockUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordLockUiEvent.NumberPress -> onNumberPress(event.number)
                is PasswordLockUiEvent.BackSpacePress -> onBackSpacePress()
                is PasswordLockUiEvent.VerifyPin -> verifyPin()
                is PasswordLockUiEvent.OpenPhoneApp -> PasswordLockUiEffect.OpenPhoneApp
            }

            if (effect is PasswordLockUiEffect) { _effectChannel.send(effect) }
        }
    }
}
