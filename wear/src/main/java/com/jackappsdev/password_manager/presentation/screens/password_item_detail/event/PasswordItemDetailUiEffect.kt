package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

sealed class PasswordItemDetailUiEffect {
    data object NavigateUp : PasswordItemDetailUiEffect()
}
