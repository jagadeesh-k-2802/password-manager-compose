package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

sealed class ManageCategoriesUiEffect {
    data object ScrollToTop : ManageCategoriesUiEffect()
}
