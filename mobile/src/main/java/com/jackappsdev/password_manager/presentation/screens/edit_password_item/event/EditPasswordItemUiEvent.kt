package com.jackappsdev.password_manager.presentation.screens.edit_password_item.event

import com.jackappsdev.password_manager.domain.model.CategoryModel

sealed class EditPasswordItemUiEvent {
    data object EditPassword : EditPasswordItemUiEvent()
    data object ToggleUnsavedChangesDialogVisibility : EditPasswordItemUiEvent()
    data object ToggleAlreadyAutoFocused : EditPasswordItemUiEvent()
    data object ToggleCategoryDropdownVisibility : EditPasswordItemUiEvent()
    data object ToggleShowPassword : EditPasswordItemUiEvent()
    data object GenerateRandomPassword : EditPasswordItemUiEvent()
    data class EnterName(val value: String) : EditPasswordItemUiEvent()
    data class EnterUsername(val value: String) : EditPasswordItemUiEvent()
    data class EnterPassword(val value: String) : EditPasswordItemUiEvent()
    data class EnterWebsite(val value: String) : EditPasswordItemUiEvent()
    data class EnterNotes(val value: String) : EditPasswordItemUiEvent()
    data class SelectCategory(val category: CategoryModel?) : EditPasswordItemUiEvent()
    data object NavigateToAddCategory : EditPasswordItemUiEvent()
    data object NavigateUp : EditPasswordItemUiEvent()
}
