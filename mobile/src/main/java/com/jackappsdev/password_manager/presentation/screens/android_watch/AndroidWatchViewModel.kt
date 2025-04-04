package com.jackappsdev.password_manager.presentation.screens.android_watch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchUiEffect
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidWatchViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(), EventDrivenViewModel<AndroidWatchUiEvent, AndroidWatchUiEffect> {

    var state by mutableStateOf(AndroidWatchState())
        private set

    private val _effectChannel = Channel<AndroidWatchUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<AndroidWatchError>()
    val errorFlow = Channel<AndroidWatchError>().receiveAsFlow()

    init {
        getInitialData()
    }

    private fun getInitialData() {
        viewModelScope.launch {
            state = state.copy(
                useAndroidWatch = userPreferencesRepository.hasAndroidWatchPinSet(),
                hasAndroidWatchPinSet = userPreferencesRepository.hasAndroidWatchPinSet()
            )
        }
    }

    private fun enterPin(pin: String) {
        if (pin.length > 4) return
        state = state.copy(pin = pin)
    }

    private fun toggleShowPin() {
        state = state.copy(showPin = !state.showPin)
    }

    private fun toggleAndroidWatchDialog() {
        state = state.copy(showDisableAndroidWatchDialog = !state.showDisableAndroidWatchDialog)
    }

    private suspend fun setupPin(): AndroidWatchUiEffect? {
        return if (state.pin.isEmpty() == true) {
            _errorChannel.send(AndroidWatchError.PinError(R.string.error_pin_not_empty))
            null
        } else {
            state = state.copy(hasAndroidWatchPinSet = true)
            userPreferencesRepository.setAndroidWatchPinSet(state.pin)
            AndroidWatchUiEffect.SetupPin(state.pin)
        }
    }

    private fun toggleAndroidWatchSharing() {
        val toggledValue = state.useAndroidWatch != true

        state = if (!toggledValue) {
            state.copy(showDisableAndroidWatchDialog = true)
        } else {
            state.copy(useAndroidWatch = true)
        }
    }

    private suspend fun disableAndroidWatchSharing(): AndroidWatchUiEffect {
        state = state.copy(useAndroidWatch = false, showDisableAndroidWatchDialog = false)
        passwordItemRepository.removePasswordsFromWatch()
        return AndroidWatchUiEffect.DisableAndroidWatchSharing
    }

    override fun onEvent(event: AndroidWatchUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is AndroidWatchUiEvent.EnterPin -> enterPin(event.pin)
                is AndroidWatchUiEvent.ToggleShowPin -> toggleShowPin()
                is AndroidWatchUiEvent.ToggleDisableAndroidWatchDialog -> toggleAndroidWatchDialog()
                is AndroidWatchUiEvent.SetupPin -> setupPin()
                is AndroidWatchUiEvent.ToggleAndroidWatch -> toggleAndroidWatchSharing()
                is AndroidWatchUiEvent.DisableAndroidWatchSharing -> disableAndroidWatchSharing()
                is AndroidWatchUiEvent.RequestPinChange -> AndroidWatchUiEffect.RequestPinChange
                is AndroidWatchUiEvent.RequestToggleAndroidWatch -> AndroidWatchUiEffect.ConfirmToggleAndroidWatch
            }

            if (effect is AndroidWatchUiEffect) _effectChannel.send(effect)
        }
    }
}
