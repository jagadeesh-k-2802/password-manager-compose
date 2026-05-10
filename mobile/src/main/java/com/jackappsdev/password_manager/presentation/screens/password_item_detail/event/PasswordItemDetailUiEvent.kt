package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

@Suppress("ArrayInDataClass")
sealed class PasswordItemDetailUiEvent {
    data object ToggleDeleteDialogVisibility : PasswordItemDetailUiEvent()
    data object ToggleShowPasswordVisibility : PasswordItemDetailUiEvent()
    data object ToggleDropDownMenuVisibility : PasswordItemDetailUiEvent()
    data object DeleteItem : PasswordItemDetailUiEvent()
    data object ToggleAddToWatch : PasswordItemDetailUiEvent()
    data class CopyText(val text: String?) : PasswordItemDetailUiEvent()
    data class LaunchUrl(val url: String) : PasswordItemDetailUiEvent()
    data object RequestToggleAddToWatch : PasswordItemDetailUiEvent()
    data object RequestDeleteItem : PasswordItemDetailUiEvent()
    data class NavigateToEditPassword(val id: Int) : PasswordItemDetailUiEvent()
    data object NavigateUp : PasswordItemDetailUiEvent()
    data class RequestExportAttachment(val bytes: ByteArray, val fileName: String, val mimeType: String) : PasswordItemDetailUiEvent()
    data class ExportAttachment(val password: String) : PasswordItemDetailUiEvent()
    data object ToggleExportAttachmentDialogVisibility : PasswordItemDetailUiEvent()
}
