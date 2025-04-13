package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

sealed class ManageCategoriesUiEffect {
    data object ScrollToTop : ManageCategoriesUiEffect()
    data class NavigateToCategoryItem(val id: Int) : ManageCategoriesUiEffect()
    data object NavigateToAddCategory : ManageCategoriesUiEffect()
    data object NavigateUp : ManageCategoriesUiEffect()
}
