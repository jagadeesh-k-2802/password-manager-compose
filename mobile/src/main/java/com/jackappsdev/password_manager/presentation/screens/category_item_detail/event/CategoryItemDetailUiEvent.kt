package com.jackappsdev.password_manager.presentation.screens.category_item_detail.event

sealed class CategoryItemDetailUiEvent {
    data object ToggleUnsavedChangesDialogVisibility : CategoryItemDetailUiEvent()
    data object ToggleCategoryItemDeleteDialogVisibility : CategoryItemDetailUiEvent()
    data object UpdateCategoryItem : CategoryItemDetailUiEvent()
    data object DeleteCategoryItem : CategoryItemDetailUiEvent()
    data class EnterName(val name: String) : CategoryItemDetailUiEvent()
    data class SelectColor(val color: String) : CategoryItemDetailUiEvent()
    data object NavigateUp : CategoryItemDetailUiEvent()
}
