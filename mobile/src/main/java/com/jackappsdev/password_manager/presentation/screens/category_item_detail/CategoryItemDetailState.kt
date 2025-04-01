package com.jackappsdev.password_manager.presentation.screens.category_item_detail

import com.jackappsdev.password_manager.domain.model.CategoryModel

data class CategoryItemDetailState(
    val categoryModel: CategoryModel? = null,
    val isChanged: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isUnsavedChangesDialogVisible: Boolean = false
)
