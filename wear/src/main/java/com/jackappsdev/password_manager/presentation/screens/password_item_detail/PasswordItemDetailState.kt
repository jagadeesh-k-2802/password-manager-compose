package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.StateFlow

data class PasswordItemDetailState(
    val passwordItem: StateFlow<PasswordItemModel?>? = null,
    val isValueAlreadySetOnce: Boolean = false,
)
