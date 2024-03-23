package com.jagadeesh.passwordmanager.presentation.screens.manage_categories

import com.jagadeesh.passwordmanager.domain.model.CategoryModel
import kotlinx.coroutines.flow.StateFlow

data class ManageCategoriesState(
    val items: StateFlow<List<CategoryModel>>? = null,
    val isLoading: Boolean = true
)
