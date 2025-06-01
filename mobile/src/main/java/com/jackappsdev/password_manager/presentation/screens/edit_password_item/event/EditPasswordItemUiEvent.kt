package com.jackappsdev.password_manager.presentation.screens.edit_password_item.event

import androidx.compose.ui.text.input.TextFieldValue
import com.jackappsdev.password_manager.domain.model.CategoryModel

sealed class EditPasswordItemUiEvent {
    data object EditPassword : EditPasswordItemUiEvent()
    data object ToggleUnsavedChangesDialogVisibility : EditPasswordItemUiEvent()
    data object ToggleAlreadyAutoFocused : EditPasswordItemUiEvent()
    data object ToggleCategoryDropdownVisibility : EditPasswordItemUiEvent()
    data object ToggleShowPassword : EditPasswordItemUiEvent()
    data object GenerateRandomPassword : EditPasswordItemUiEvent()
    data class EnterName(val textFieldValue: TextFieldValue) : EditPasswordItemUiEvent()
    data class EnterUsername(val textFieldValue: TextFieldValue) : EditPasswordItemUiEvent()
    data class EnterPassword(val textFieldValue: TextFieldValue) : EditPasswordItemUiEvent()
    data class EnterWebsite(val textFieldValue: TextFieldValue) : EditPasswordItemUiEvent()
    data class EnterNotes(val textFieldValue: TextFieldValue) : EditPasswordItemUiEvent()
    data class SelectCategory(val category: CategoryModel?) : EditPasswordItemUiEvent()
    data object NavigateToAddCategory : EditPasswordItemUiEvent()
    data object NavigateUp : EditPasswordItemUiEvent()
}
