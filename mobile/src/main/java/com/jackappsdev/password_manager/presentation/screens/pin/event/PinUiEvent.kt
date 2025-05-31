package com.jackappsdev.password_manager.presentation.screens.pin.event

sealed class PinUiEvent {
    data class EnterPin(val pin: String) : PinUiEvent()
    data object DisablePin : PinUiEvent()
    data object ToggleShowPinVisibility : PinUiEvent()
    data object ToggleDisablePinDialogVisibility : PinUiEvent()
    data object TogglePin : PinUiEvent()
    data object ChangePin : PinUiEvent()
    data object NavigateUp : PinUiEvent()
}
