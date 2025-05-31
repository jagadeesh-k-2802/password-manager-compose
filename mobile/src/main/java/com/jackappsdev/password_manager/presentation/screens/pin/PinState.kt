package com.jackappsdev.password_manager.presentation.screens.pin

import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class PinState(
    val pin: String = EMPTY_STRING,
    val hasPinSet: Boolean = false,
    val hasAlreadyPinSet: Boolean = false,
    val showPin: Boolean = false,
    val showDisablePinDialog: Boolean = false
)
