package com.jackappsdev.password_manager.presentation.screens.add_password_item

import com.jackappsdev.password_manager.domain.model.CategoryModel

data class AddPasswordItemState(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val website: String = "",
    val notes: String = "",
    val category: CategoryModel? = null,
    val showPassword: Boolean = false,
    val isCategoryDropdownVisible: Boolean = false,
    val isUnsavedChangesDialogVisible: Boolean = false,
    val isAlreadyAutoFocused: Boolean = false,
    val hasUserEnteredDetails: Boolean = false
)
