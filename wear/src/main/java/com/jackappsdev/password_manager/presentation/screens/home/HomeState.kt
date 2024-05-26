package com.jackappsdev.password_manager.presentation.screens.home

import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.StateFlow

data class HomeState(
    val items: StateFlow<List<PasswordItemModel>>? = null,
    val isLoading: Boolean = true
)
