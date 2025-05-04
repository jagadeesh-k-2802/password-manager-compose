package com.jackappsdev.password_manager.presentation.screens.password_generator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.core.getPasswordStrengthColor
import com.jackappsdev.password_manager.core.getPasswordStrengthColorDark
import com.jackappsdev.password_manager.core.getPasswordStrengthText
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.ZERO
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

    companion object {
        const val LAST_OPTION_COUNT = 3
    }

    init {
        onInit()
    }

    private fun onInit() {
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
                passwordStrengthText = getPasswordStrengthText(passwordLength),
                passwordStrengthColor = getPasswordStrengthColor(passwordLength),
                passwordStrengthColorDark = getPasswordStrengthColorDark(passwordLength)
            )
        }
    }

    private fun copyToClipboard(): PasswordGeneratorUiEffect {
        return PasswordGeneratorUiEffect.CopyToClipboard(state.password)
    }

    private fun onLengthChange(length: Int) {
        if (length == state.passwordLength) return
        state = state.copy(passwordLength = length)
        onInit()
    }

    private fun onOptionToggle(event: PasswordGeneratorUiEvent): PasswordGeneratorUiEffect? {
        when (event) {
            is PasswordGeneratorUiEvent.ToggleIncludeLowercase -> {
                val value = !state.includeLowercase
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeLowercase = value)
            }

            is PasswordGeneratorUiEvent.ToggleIncludeUppercase -> {
                val value = !state.includeUppercase
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeUppercase = value)
            }

            is PasswordGeneratorUiEvent.ToggleIncludeNumbers -> {
                val value = !state.includeNumbers
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeNumbers = value)
            }

            is PasswordGeneratorUiEvent.ToggleIncludeSymbols -> {
                val value = !state.includeSymbols
                val effect = isLastOption(value)
                if (effect != null) return effect
                state = state.copy(includeSymbols = value)
            }

            else -> {
                null
            }
        }

        onInit()
        return null
    }

    private fun isLastOption(value: Boolean): PasswordGeneratorUiEffect? {
        var count = ZERO
        if (!state.includeLowercase) count++
        if (!state.includeUppercase) count++
        if (!state.includeNumbers) count++
        if (!state.includeSymbols) count++

        return if (count == LAST_OPTION_COUNT && !value) {
            PasswordGeneratorUiEffect.ShowSnackbarMessage(R.string.text_all_options_warning)
        } else {
            null
        }
    }

    override fun onEvent(event: PasswordGeneratorUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordGeneratorUiEvent.RegeneratePassword -> onInit()
                is PasswordGeneratorUiEvent.CopyPassword -> copyToClipboard()
                is PasswordGeneratorUiEvent.LengthChange -> onLengthChange(event.length)
                is PasswordGeneratorUiEvent.ToggleIncludeLowercase -> onOptionToggle(event)
                is PasswordGeneratorUiEvent.ToggleIncludeUppercase -> onOptionToggle(event)
                is PasswordGeneratorUiEvent.ToggleIncludeNumbers -> onOptionToggle(event)
                is PasswordGeneratorUiEvent.ToggleIncludeSymbols -> onOptionToggle(event)
            }

            if (effect is PasswordGeneratorUiEffect) { _effectChannel.send(effect) }
        }
    }
}
