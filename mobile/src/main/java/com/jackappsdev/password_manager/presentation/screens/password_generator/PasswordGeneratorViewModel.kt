package com.jackappsdev.password_manager.presentation.screens.password_generator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordGeneratorViewModel @Inject constructor() : ViewModel(),
    EventDrivenViewModel<PasswordGeneratorUiEvent, PasswordGeneratorUiEffect> {

    var state by mutableStateOf(PasswordGeneratorState())
        private set

    private val _effectChannel = Channel<PasswordGeneratorUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        generatePassword()
    }

    private fun generatePassword() {
        with(state) {
            state = state.copy(
                password = generateRandomPassword(
                    GeneratePasswordConfig(
                        passwordLength,
                        includeLowercase,
                        includeUppercase,
                        includeNumbers,
                        includeSymbols
                    )
                ),
                passwordStrengthText = when (passwordLength) {
                    in (1..8) -> R.string.text_weak
                    in (8..12) -> R.string.text_medium
                    else -> R.string.text_strong
                },
                passwordStrengthColor = when (passwordLength) {
                    in (1..8) -> parseColor("#FF8C69")
                    in (8..12) -> parseColor("#FFD700")
                    else -> parseColor("#32CD32")
                },
                passwordStrengthColorDark = when (passwordLength) {
                    in (1..8) -> parseColor("#A61B11")
                    in (8..12) -> parseColor("#D1A000")
                    else -> parseColor("#3D9050")
                }
            )
        }
    }

    private fun onLengthChange(length: Int) {
        if (length == state.passwordLength) return
        state = state.copy(passwordLength = length)
        generatePassword()
    }

    private fun copyToClipboard(): PasswordGeneratorUiEffect {
        return PasswordGeneratorUiEffect.OnCopyToClipboard(state.password)
    }

    private fun isLastOption(value: Boolean): PasswordGeneratorUiEffect? {
        var count = 0
        if (!state.includeLowercase) count++
        if (!state.includeUppercase) count++
        if (!state.includeNumbers) count++
        if (!state.includeSymbols) count++

        return if (count == 3 && !value) {
            PasswordGeneratorUiEffect.OnShowSnackbarMessage(R.string.text_all_options_warning)
        } else {
            null
        }
    }

    private fun onOptionToggle(event: PasswordGeneratorUiEvent): PasswordGeneratorUiEffect? {
        when (event) {
            is PasswordGeneratorUiEvent.OnToggleIncludeLowercase -> {
                val value = !state.includeLowercase
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeLowercase = value)
            }

            is PasswordGeneratorUiEvent.OnToggleIncludeUppercase -> {
                val value = !state.includeUppercase
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeUppercase = value)
            }

            is PasswordGeneratorUiEvent.OnToggleIncludeNumbers -> {
                val value = !state.includeNumbers
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeNumbers = value)
            }

            is PasswordGeneratorUiEvent.OnToggleIncludeSymbols -> {
                val value = !state.includeSymbols
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeSymbols = value)
            }

            else -> {
                // Nothing To Handle
            }
        }

        generatePassword()
        return null
    }

    override fun onEvent(event: PasswordGeneratorUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordGeneratorUiEvent.OnCopyPassword -> copyToClipboard()
                is PasswordGeneratorUiEvent.RegeneratePassword -> generatePassword()
                is PasswordGeneratorUiEvent.OnLengthChange -> onLengthChange(event.length)
                is PasswordGeneratorUiEvent.OnToggleIncludeLowercase -> onOptionToggle(event)
                is PasswordGeneratorUiEvent.OnToggleIncludeUppercase -> onOptionToggle(event)
                is PasswordGeneratorUiEvent.OnToggleIncludeNumbers -> onOptionToggle(event)
                is PasswordGeneratorUiEvent.OnToggleIncludeSymbols -> onOptionToggle(event)
            }

            if (effect is PasswordGeneratorUiEffect) { _effectChannel.send(effect) }
        }
    }
}
