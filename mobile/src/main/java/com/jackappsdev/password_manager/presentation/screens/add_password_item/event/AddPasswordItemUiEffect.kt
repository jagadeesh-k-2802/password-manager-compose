package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel

sealed class AddPasswordItemUiEffect {
    data object NavigateToAddCategory : AddPasswordItemUiEffect()
    data class NavigateUp(val createdPassword: PasswordWithCategoryModel? = null) : AddPasswordItemUiEffect()
    data class OpenExportAttachmentIntent(val fileName: String, val mimeType: String) : AddPasswordItemUiEffect()
}
