package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

sealed class ManageCategoriesUiEvent {
    data object ScrollToTop : ManageCategoriesUiEvent()
}
