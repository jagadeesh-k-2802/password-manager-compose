package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import kotlinx.coroutines.flow.StateFlow

data class PasswordItemDetailState(
    val passwordItem: StateFlow<PasswordWithCategoryModel?>? = null,
    val hasAndroidWatchPinSet: Boolean? = null,
    val showPassword: Boolean = false,
    val dropDownMenuExpanded: Boolean = false,
    val isDeleteDialogVisible: Boolean = false
)
