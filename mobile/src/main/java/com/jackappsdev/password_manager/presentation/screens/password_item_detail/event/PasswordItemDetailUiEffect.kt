package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

sealed class PasswordItemDetailUiEffect {
    data class CopyText(val text: String?) : PasswordItemDetailUiEffect()
    data class LaunchUrl(val url: String) : PasswordItemDetailUiEffect()
    data object DeleteItem : PasswordItemDetailUiEffect()
    data object ToggleIsAddedToWatch : PasswordItemDetailUiEffect()
    data class NavigateToEditPassword(val id: Int) : PasswordItemDetailUiEffect()
    data object NavigateUp : PasswordItemDetailUiEffect()
}
