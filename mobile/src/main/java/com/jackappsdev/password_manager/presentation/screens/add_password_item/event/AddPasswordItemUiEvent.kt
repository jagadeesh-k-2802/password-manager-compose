package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import com.jackappsdev.password_manager.domain.model.CategoryModel

sealed class AddPasswordItemUiEvent {
    data class EnterName(val name: String) : AddPasswordItemUiEvent()
    data class EnterUsername(val username: String) : AddPasswordItemUiEvent()
    data class EnterPassword(val password: String) : AddPasswordItemUiEvent()
    data class EnterWebsite(val website: String) : AddPasswordItemUiEvent()
    data class EnterNotes(val notes: String) : AddPasswordItemUiEvent()
    data object GenerateRandomPassword : AddPasswordItemUiEvent()
    data object ToggleAlreadyAutoFocusVisibility : AddPasswordItemUiEvent()
    data object ToggleShowPasswordVisibility : AddPasswordItemUiEvent()
    data object ToggleCategoryDropdownVisibility : AddPasswordItemUiEvent()
    data object ToggleUnsavedDialogVisibility : AddPasswordItemUiEvent()
    data class SelectCategory(val category: CategoryModel?) : AddPasswordItemUiEvent()
    data object AddPasswordItem : AddPasswordItemUiEvent()
    data object NavigateToAddCategory : AddPasswordItemUiEvent()
    data object NavigateUp : AddPasswordItemUiEvent()
}
