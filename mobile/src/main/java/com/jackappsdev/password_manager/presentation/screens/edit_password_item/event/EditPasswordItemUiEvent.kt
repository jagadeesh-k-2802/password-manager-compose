package com.jackappsdev.password_manager.presentation.screens.edit_password_item.event

import com.jackappsdev.password_manager.domain.model.CategoryModel

sealed class EditPasswordItemUiEvent {
    data object EditPassword : EditPasswordItemUiEvent()
    data object ToggleUnsavedChangesDialogVisibility : EditPasswordItemUiEvent()
    data object ToggleIsAlreadyAutoFocused : EditPasswordItemUiEvent()
    data object ToggleCategoryDropdownVisibility : EditPasswordItemUiEvent()
    data object ToggleShowPassword : EditPasswordItemUiEvent()
    data object OnGenerateRandomPassword : EditPasswordItemUiEvent()
    data class OnEnterName(val text: String) : EditPasswordItemUiEvent()
    data class OnEnterUsername(val text: String) : EditPasswordItemUiEvent()
    data class OnEnterPassword(val text: String) : EditPasswordItemUiEvent()
    data class OnEnterWebsite(val text: String) : EditPasswordItemUiEvent()
    data class OnEnterNotes(val text: String) : EditPasswordItemUiEvent()
    data class OnSelectCategory(val category: CategoryModel?) : EditPasswordItemUiEvent()
    data object NavigateUp : EditPasswordItemUiEvent()
    data object NavigateToAddCategory : EditPasswordItemUiEvent()
}
