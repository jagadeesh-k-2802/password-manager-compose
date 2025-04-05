package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import com.jackappsdev.password_manager.domain.model.CategoryModel

sealed class AddPasswordItemUiEvent {
    data class OnEnterName(val name: String) : AddPasswordItemUiEvent()
    data class OnEnterUsername(val username: String) : AddPasswordItemUiEvent()
    data class OnEnterPassword(val password: String) : AddPasswordItemUiEvent()
    data class OnEnterWebsite(val website: String) : AddPasswordItemUiEvent()
    data class OnEnterNotes(val notes: String) : AddPasswordItemUiEvent()
    data object AddPasswordItem : AddPasswordItemUiEvent()
    data object OnGenerateRandomPassword : AddPasswordItemUiEvent()
    data object ToggleShowPassword : AddPasswordItemUiEvent()
    data object ToggleIsCategoryDropdownVisibility : AddPasswordItemUiEvent()
    data object ToggleIsUnsavedDialogVisibility : AddPasswordItemUiEvent()
    data object SetIsAlreadyAutoFocus : AddPasswordItemUiEvent()
    data class OnSelectCategory(val category: CategoryModel?) : AddPasswordItemUiEvent()
}
