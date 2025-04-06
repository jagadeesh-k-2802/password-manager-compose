package com.jackappsdev.password_manager.presentation.screens.home.event

sealed class HomeUiEvent {
    data class NavigateToPasswordDetail(val id: Int) : HomeUiEvent()
}
