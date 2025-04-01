package com.jackappsdev.password_manager.presentation.screens.add_category_item.event

import com.jackappsdev.password_manager.domain.model.CategoryModel

sealed class AddCategoryItemUiEffect {
    data class NavigateUp(val model: CategoryModel) : AddCategoryItemUiEffect()
}
