package com.jackappsdev.password_manager.presentation.screens.manage_categories

import com.jackappsdev.password_manager.domain.model.CategoryModel
import kotlinx.coroutines.flow.StateFlow

data class ManageCategoriesState(
    val items: StateFlow<List<CategoryModel>>? = null,
    val isLoading: Boolean = true
)
