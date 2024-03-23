package com.jagadeesh.passwordmanager.presentation.screens.add_category_item

sealed interface AddCategoryItemError {
    data class NameError(val error: String) : AddCategoryItemError
}
