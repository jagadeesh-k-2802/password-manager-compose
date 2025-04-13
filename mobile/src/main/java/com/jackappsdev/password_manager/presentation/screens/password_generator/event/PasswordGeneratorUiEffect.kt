package com.jackappsdev.password_manager.presentation.screens.password_generator.event

import androidx.annotation.StringRes

sealed class PasswordGeneratorUiEffect {
    data class CopyToClipboard(val text: String) : PasswordGeneratorUiEffect()
    data class ShowSnackbarMessage(@StringRes val message: Int) : PasswordGeneratorUiEffect()
}
