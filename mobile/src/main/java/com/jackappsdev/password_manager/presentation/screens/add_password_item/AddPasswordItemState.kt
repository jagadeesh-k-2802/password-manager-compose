package com.jackappsdev.password_manager.presentation.screens.add_password_item

import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class AddPasswordItemState(
    val name: String = EMPTY_STRING,
    val username: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val website: String = EMPTY_STRING,
    val notes: String = EMPTY_STRING,
    val category: CategoryModel? = null,
    val showPassword: Boolean = false,
    val isCategoryDropdownVisible: Boolean = false,
    val isUnsavedChangesDialogVisible: Boolean = false,
    val isAlreadyAutoFocused: Boolean = false,
    val hasUserEnteredDetails: Boolean = false
)
