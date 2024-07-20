package com.jackappsdev.password_manager.presentation.screens.android_watch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidWatchViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var state by mutableStateOf(AndroidWatchState())
        private set

    val errorChannel = Channel<AndroidWatchError>()

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

    fun setUseAndroidWatch(newValue: Boolean) {
        viewModelScope.launch {
            state = state.copy(useAndroidWatch = newValue)

            if (!newValue) {
                passwordItemRepository.removePasswordsFromWatch()
            }
        }
    }

    fun setAndroidWatchPin(newPin: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (newPin?.isEmpty() == true) {
                errorChannel.send(AndroidWatchError.PinError(R.string.error_pin_not_empty))
            } else {
                state = state.copy(hasAndroidWatchPinSet = true)
                userPreferencesRepository.setAndroidWatchPinSet(newPin)
                onSuccess()
            }
        }
    }
}
