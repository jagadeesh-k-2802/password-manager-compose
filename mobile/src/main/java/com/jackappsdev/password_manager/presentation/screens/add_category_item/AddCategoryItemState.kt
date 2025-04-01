package com.jackappsdev.password_manager.presentation.screens.add_category_item

import com.jackappsdev.password_manager.shared.constants.colorList

data class AddCategoryItemState(
    val name: String = "",
    val color: String = colorList.first(),
    val isUnsavedChangesDialogVisible: Boolean = false
)
