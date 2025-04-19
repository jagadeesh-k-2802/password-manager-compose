package com.jackappsdev.password_manager.presentation.screens.add_category_item.event

sealed class AddCategoryItemUiEvent {
    data class EnterName(val name: String) : AddCategoryItemUiEvent()
    data class SelectColor(val color: String) : AddCategoryItemUiEvent()
    data object ToggleUnsavedChangesDialogVisibility : AddCategoryItemUiEvent()
    data object AddCategoryItem : AddCategoryItemUiEvent()
    data object NavigateUp : AddCategoryItemUiEvent()
}
