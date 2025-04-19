package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

sealed class PasswordItemDetailUiEvent {
    data object ToggleAlreadySetOnce : PasswordItemDetailUiEvent()
    data object NavigateUp : PasswordItemDetailUiEvent()
}
