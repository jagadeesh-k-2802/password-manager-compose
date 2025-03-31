package com.jackappsdev.password_manager.presentation.screens.password_generator.event

sealed class PasswordGeneratorUiEvent {
    data object OnCopyPassword : PasswordGeneratorUiEvent()
    data object RegeneratePassword : PasswordGeneratorUiEvent()
    data class OnLengthChange(val length: Int) : PasswordGeneratorUiEvent()
    data object OnToggleIncludeLowercase : PasswordGeneratorUiEvent()
    data object OnToggleIncludeUppercase : PasswordGeneratorUiEvent()
    data object OnToggleIncludeNumbers : PasswordGeneratorUiEvent()
    data object OnToggleIncludeSymbols : PasswordGeneratorUiEvent()
}
