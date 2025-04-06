package com.jackappsdev.password_manager.presentation.screens.home.event

sealed class HomeUiEffect {
    data class NavigateToPasswordDetail(val id: Int) : HomeUiEffect()
}
