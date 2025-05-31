package com.jackappsdev.password_manager.presentation.screens.pin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinUiEffect
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(), EventDrivenViewModel<PinUiEvent, PinUiEffect> {

    var state by mutableStateOf(PinState())
        private set

    private val _effectChannel = Channel<PinUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<PinError>()
    val errorFlow = Channel<PinError>().receiveAsFlow()

    companion object {
        private const val MAX_PIN_LENGTH = 8
    }

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            state = state.copy(
                hasPinSet = userPreferencesRepository.hasPinSet(),
                hasAlreadyPinSet = userPreferencesRepository.hasPinSet(),
            )
        }
    }

    private fun enterPin(pin: String) {
        if (pin.length > MAX_PIN_LENGTH) return
        state = state.copy(pin = pin)
    }

    private suspend fun disablePin() {
        userPreferencesRepository.setPin(EMPTY_STRING)
        state = state.copy(hasPinSet = false, hasAlreadyPinSet = false, showDisablePinDialog = false)
    }

    private fun toggleVisibility(event: PinUiEvent) {
        when (event) {
            is PinUiEvent.ToggleShowPinVisibility -> {
                state = state.copy(showPin = !state.showPin)
            }

            is PinUiEvent.ToggleDisablePinDialogVisibility -> {
                state = state.copy(showDisablePinDialog = !state.showDisablePinDialog)
            }

            else -> {
                null
            }
        }
    }

    private fun togglePin() {
        state = if (state.hasAlreadyPinSet) {
            state.copy(showDisablePinDialog = true)
        } else {
            state.copy(hasPinSet = true)
        }
    }

    private suspend fun changePin(): PinUiEffect? {
        if (state.pin.isEmpty()) {
            _errorChannel.send(PinError.PinInputError(R.string.error_pin_not_empty))
            return null
        } else {
            userPreferencesRepository.setPin(state.pin)
            state = state.copy(hasPinSet = true, hasAlreadyPinSet = true, pin = EMPTY_STRING)
            return PinUiEffect.PinUpdated
        }
    }

    override fun onEvent(event: PinUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PinUiEvent.EnterPin -> enterPin(event.pin)
                is PinUiEvent.DisablePin -> disablePin()
                is PinUiEvent.ToggleShowPinVisibility -> toggleVisibility(event)
                is PinUiEvent.ToggleDisablePinDialogVisibility -> toggleVisibility(event)
                is PinUiEvent.TogglePin -> togglePin()
                is PinUiEvent.ChangePin -> changePin()
                is PinUiEvent.NavigateUp -> PinUiEffect.NavigateUp
            }

            if (effect is PinUiEffect) { _effectChannel.send(effect) }
        }
    }
}
