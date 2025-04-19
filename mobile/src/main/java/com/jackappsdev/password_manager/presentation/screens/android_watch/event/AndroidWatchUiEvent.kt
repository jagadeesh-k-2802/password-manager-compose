package com.jackappsdev.password_manager.presentation.screens.android_watch.event

sealed class AndroidWatchUiEvent {
    data object ToggleDisableAndroidWatchDialogVisibility : AndroidWatchUiEvent()
    data object ToggleShowPinVisibility : AndroidWatchUiEvent()
    data object ToggleAndroidWatch : AndroidWatchUiEvent()
    data class EnterPin(val pin: String) : AndroidWatchUiEvent()
    data object SetupPin : AndroidWatchUiEvent()
    data object DisableAndroidWatchSharing : AndroidWatchUiEvent()
    data object RequestPinChange : AndroidWatchUiEvent()
    data object RequestToggleAndroidWatch : AndroidWatchUiEvent()
    data object NavigateUp : AndroidWatchUiEvent()
}
