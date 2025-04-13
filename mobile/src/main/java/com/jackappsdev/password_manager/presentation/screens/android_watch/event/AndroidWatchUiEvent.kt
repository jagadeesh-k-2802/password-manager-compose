package com.jackappsdev.password_manager.presentation.screens.android_watch.event

sealed class AndroidWatchUiEvent {
    data class EnterPin(val pin: String) : AndroidWatchUiEvent()
    data object ToggleShowPin : AndroidWatchUiEvent()
    data object ToggleDisableAndroidWatchDialog : AndroidWatchUiEvent()
    data object SetupPin : AndroidWatchUiEvent()
    data object ToggleAndroidWatch : AndroidWatchUiEvent()
    data object DisableAndroidWatchSharing : AndroidWatchUiEvent()
    data object RequestPinChange : AndroidWatchUiEvent()
    data object RequestToggleAndroidWatch : AndroidWatchUiEvent()
    data object NavigateUp : AndroidWatchUiEvent()
}
