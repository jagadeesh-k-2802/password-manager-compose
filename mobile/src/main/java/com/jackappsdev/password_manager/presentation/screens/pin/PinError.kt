package com.jackappsdev.password_manager.presentation.screens.pin

import androidx.annotation.StringRes

sealed interface PinError {
    data class PinInputError(@StringRes val error: Int) : PinError
}
