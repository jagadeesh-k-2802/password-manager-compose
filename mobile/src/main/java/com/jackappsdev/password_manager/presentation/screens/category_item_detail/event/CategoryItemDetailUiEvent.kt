package com.jackappsdev.password_manager.presentation.screens.category_item_detail.event

sealed class CategoryItemDetailUiEvent {
    data object UpdateCategoryItem : CategoryItemDetailUiEvent()
    data object DeleteCategoryItem : CategoryItemDetailUiEvent()
    data class OnEnterName(val name: String) : CategoryItemDetailUiEvent()
    data class OnSelectColor(val color: String) : CategoryItemDetailUiEvent()
    data object ToggleUnsavedChangesDialog : CategoryItemDetailUiEvent()
    data object ToggleCategoryItemDeleteDialog : CategoryItemDetailUiEvent()
    data object NavigateUp : CategoryItemDetailUiEvent()
}
