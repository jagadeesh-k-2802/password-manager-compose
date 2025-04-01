package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.annotation.StringRes

sealed interface AddCategoryItemError {
    data class NameError(@StringRes val error: Int) : AddCategoryItemError
}
