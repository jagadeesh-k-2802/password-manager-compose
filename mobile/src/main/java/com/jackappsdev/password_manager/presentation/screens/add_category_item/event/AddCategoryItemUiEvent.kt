package com.jackappsdev.password_manager.presentation.screens.add_category_item.event

sealed class AddCategoryItemUiEvent {
    data object AddCategoryItem : AddCategoryItemUiEvent()
    data object ToggleUnsavedChangesDialog : AddCategoryItemUiEvent()
    data class OnEnterName(val name: String) : AddCategoryItemUiEvent()
    data class OnSelectColor(val color: String) : AddCategoryItemUiEvent()
}
