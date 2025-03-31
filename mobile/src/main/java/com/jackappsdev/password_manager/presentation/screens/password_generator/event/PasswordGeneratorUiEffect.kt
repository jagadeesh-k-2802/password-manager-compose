package com.jackappsdev.password_manager.presentation.screens.password_generator.event

import androidx.annotation.StringRes

sealed class PasswordGeneratorUiEffect {
    data class OnCopyToClipboard(val text: String) : PasswordGeneratorUiEffect()
    data class OnShowSnackbarMessage(@StringRes val message: Int) : PasswordGeneratorUiEffect()
}
