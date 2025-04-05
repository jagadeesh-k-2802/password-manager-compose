package com.jackappsdev.password_manager.presentation.screens.password_item_detail

data class PasswordItemDetailState(
    val hasAndroidWatchPinSet: Boolean? = null,
    val showPassword: Boolean = false,
    val dropDownMenuExpanded: Boolean = false,
    val isDeleteDialogVisible: Boolean = false
)
