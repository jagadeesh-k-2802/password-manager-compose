package com.jackappsdev.password_manager.presentation.screens.add_category_item

import com.jackappsdev.password_manager.constants.colorList
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class AddCategoryItemState(
    val name: String = EMPTY_STRING,
    val color: String = colorList.first(),
    val isUnsavedChangesDialogVisible: Boolean = false
)
