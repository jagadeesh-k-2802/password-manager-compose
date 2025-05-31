package com.jackappsdev.password_manager.presentation.screens.pin.event

sealed class PinUiEffect {
    data object PinUpdated : PinUiEffect()
    data object NavigateUp : PinUiEffect()
}
