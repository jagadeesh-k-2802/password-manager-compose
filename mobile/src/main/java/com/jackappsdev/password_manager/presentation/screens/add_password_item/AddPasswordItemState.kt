package com.jackappsdev.password_manager.presentation.screens.add_password_item

import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.model.AttachmentToExport
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import com.jackappsdev.password_manager.shared.constants.ZERO
import kotlinx.coroutines.flow.StateFlow

data class AddPasswordItemState(
    val name: String = EMPTY_STRING,
    val username: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val website: String = EMPTY_STRING,
    val notes: String = EMPTY_STRING,
    val images: List<ByteArray> = emptyList(),
    val category: CategoryModel? = null,
    val categoryItems: StateFlow<List<CategoryModel>>? = null,
    val showPassword: Boolean = false,
    val isCategoryDropdownVisible: Boolean = false,
    val isUnsavedChangesDialogVisible: Boolean = false,
    val isAlreadyAutoFocused: Boolean = false,
    val hasUserEnteredDetails: Boolean = false,
    val isExportAttachmentDialogVisible: Boolean = false,
    val isExportAttachmentPasswordInvalid: Boolean = false,
    val attachmentToExport: AttachmentToExport? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AddPasswordItemState) return false
        if (name != other.name) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (website != other.website) return false
        if (notes != other.notes) return false
        if (category != other.category) return false
        if (categoryItems != other.categoryItems) return false
        if (showPassword != other.showPassword) return false
        if (isCategoryDropdownVisible != other.isCategoryDropdownVisible) return false
        if (isUnsavedChangesDialogVisible != other.isUnsavedChangesDialogVisible) return false
        if (isAlreadyAutoFocused != other.isAlreadyAutoFocused) return false
        if (hasUserEnteredDetails != other.hasUserEnteredDetails) return false
        if (isExportAttachmentDialogVisible != other.isExportAttachmentDialogVisible) return false
        if (isExportAttachmentPasswordInvalid != other.isExportAttachmentPasswordInvalid) return false
        if (attachmentToExport != other.attachmentToExport) return false
        if (images.size != other.images.size) return false
        for (i in images.indices) {
            if (!images[i].contentEquals(other.images[i])) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + website.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + (category?.hashCode() ?: ZERO)
        result = 31 * result + (categoryItems?.hashCode() ?: ZERO)
        result = 31 * result + showPassword.hashCode()
        result = 31 * result + isCategoryDropdownVisible.hashCode()
        result = 31 * result + isUnsavedChangesDialogVisible.hashCode()
        result = 31 * result + isAlreadyAutoFocused.hashCode()
        result = 31 * result + hasUserEnteredDetails.hashCode()
        result = 31 * result + isExportAttachmentDialogVisible.hashCode()
        result = 31 * result + isExportAttachmentPasswordInvalid.hashCode()
        result = 31 * result + (attachmentToExport?.hashCode() ?: ZERO)
        result = 31 * result + images.sumOf { it.contentHashCode() }
        return result
    }
}
