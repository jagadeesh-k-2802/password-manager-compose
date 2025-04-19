package com.jackappsdev.password_manager.presentation.screens.password_generator.event

sealed class PasswordGeneratorUiEvent {
    data object RegeneratePassword : PasswordGeneratorUiEvent()
    data object CopyPassword : PasswordGeneratorUiEvent()
    data class LengthChange(val length: Int) : PasswordGeneratorUiEvent()
    data object ToggleIncludeLowercase : PasswordGeneratorUiEvent()
    data object ToggleIncludeUppercase : PasswordGeneratorUiEvent()
    data object ToggleIncludeNumbers : PasswordGeneratorUiEvent()
    data object ToggleIncludeSymbols : PasswordGeneratorUiEvent()
}
