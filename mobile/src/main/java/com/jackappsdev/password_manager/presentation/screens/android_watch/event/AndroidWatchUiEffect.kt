package com.jackappsdev.password_manager.presentation.screens.android_watch.event

sealed class AndroidWatchUiEffect {
    data object RequestPinChange : AndroidWatchUiEffect()
    data class SetupPin(val pin: String) : AndroidWatchUiEffect()
    data object ConfirmToggleAndroidWatch : AndroidWatchUiEffect()
    data object DisableAndroidWatchSharing : AndroidWatchUiEffect()
    data object NavigateUp : AndroidWatchUiEffect()
}
