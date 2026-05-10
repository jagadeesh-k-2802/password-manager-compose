package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

sealed class AddPasswordItemUiEffect {
    data object NavigateToAddCategory : AddPasswordItemUiEffect()
    data object NavigateUp : AddPasswordItemUiEffect()
    data class OpenExportAttachmentIntent(val fileName: String, val mimeType: String) : AddPasswordItemUiEffect()
}
