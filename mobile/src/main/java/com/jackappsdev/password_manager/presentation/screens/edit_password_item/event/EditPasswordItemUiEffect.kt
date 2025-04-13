package com.jackappsdev.password_manager.presentation.screens.edit_password_item.event

sealed class EditPasswordItemUiEffect {
    data object OnEditComplete : EditPasswordItemUiEffect()
    data object NavigateUp : EditPasswordItemUiEffect()
    data object NavigateToAddCategory : EditPasswordItemUiEffect()
}
