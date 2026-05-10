package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.domain.mappers.toCategoryModel
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.model.AttachmentToExport
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditPasswordItemViewModel.Factory::class)
class EditPasswordItemViewModel @AssistedInject constructor(
    application: Application,
    @Assisted val editPasswordItem: Routes.EditPasswordItem,
    private val passwordItemRepository: PasswordItemRepository,
    private val categoryRepository: CategoryRepository,
    private val userPreferencesRepository: com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
) : ViewModel(), EventDrivenViewModel<EditPasswordItemUiEvent, EditPasswordItemUiEffect> {

    @AssistedFactory
    interface Factory {
        fun create(editPasswordItem: Routes.EditPasswordItem): EditPasswordItemViewModel
    }

    var state by mutableStateOf(EditPasswordItemState())
        private set

    private val passwordItem = passwordItemRepository.getPasswordItem(editPasswordItem.id)
    private val noCategoryModel = CategoryModel(name = application.getString(R.string.text_no_category), color = "#000000")

    private val _errorChannel = Channel<EditPasswordItemError>()
    val errorFlow = _errorChannel.receiveAsFlow()

    private val _effectChannel = Channel<EditPasswordItemUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            val categoryItems = categoryRepository.getAllCategories()
            state = state.copy(categoryItems = categoryItems.stateIn(viewModelScope))

            passwordItem.collect { item ->
                state = state.copy(
                    passwordItem = item,
                    category = item?.toCategoryModel()
                )
            }
        }
    }

    private suspend fun editPassword(): EditPasswordItemUiEffect? {
        if (state.passwordItem == null) return null

        if (state.passwordItem?.name?.isEmpty() == true) {
            _errorChannel.send(EditPasswordItemError.NameError(R.string.error_name_not_empty))
            return null
        } else {
            val newPasswordItemModel = PasswordItemModel(
                id = state.passwordItem?.id,
                name = state.passwordItem?.name ?: EMPTY_STRING,
                username = state.passwordItem?.username ?: EMPTY_STRING,
                password = state.passwordItem?.password ?: EMPTY_STRING,
                website = state.passwordItem?.website ?: EMPTY_STRING,
                notes = state.passwordItem?.notes ?: EMPTY_STRING,
                categoryId = state.category?.id,
                images = state.passwordItem?.images ?: emptyList(),
                isAddedToWatch = state.passwordItem?.isAddedToWatch == true,
                createdAt = System.currentTimeMillis()
            )

            passwordItemRepository.upsertPasswordItem(newPasswordItemModel)
            return EditPasswordItemUiEffect.EditComplete
        }
    }

    private fun toggleVisibility(event: EditPasswordItemUiEvent) {
        when (event) {
            is EditPasswordItemUiEvent.ToggleUnsavedChangesDialogVisibility -> {
                state = state.copy(isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible)
            }

            is EditPasswordItemUiEvent.ToggleAlreadyAutoFocused -> {
                state = state.copy(isAlreadyAutoFocused = !state.isAlreadyAutoFocused)
            }

            is EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility -> {
                state = state.copy(isCategoryDropdownVisible = !state.isCategoryDropdownVisible)
            }

            is EditPasswordItemUiEvent.ToggleShowPassword -> {
                state = state.copy(showPassword = !state.showPassword)
            }

            else -> {
                // No Operation
            }
        }
    }

    private fun onGenerateRandomPassword() {
        val password = generateRandomPassword(GeneratePasswordConfig(length = 13))
        state = state.copy(passwordItem = state.passwordItem?.copy(password = password), isChanged = true)
    }

    private fun onEnterText(event: EditPasswordItemUiEvent) {
        val passwordItem = when (event) {
            is EditPasswordItemUiEvent.EnterName -> state.passwordItem?.copy(name = event.value)
            is EditPasswordItemUiEvent.EnterUsername -> state.passwordItem?.copy(username = event.value)
            is EditPasswordItemUiEvent.EnterPassword -> state.passwordItem?.copy(password = event.value)
            is EditPasswordItemUiEvent.EnterWebsite -> state.passwordItem?.copy(website = event.value)
            is EditPasswordItemUiEvent.EnterNotes -> state.passwordItem?.copy(notes = event.value)
            else -> null
        }

        passwordItem?.let {
            state = state.copy(passwordItem = passwordItem, isChanged = true)
        }
    }

    private fun onAddImage(image: ByteArray) {
        val current = state.passwordItem?.images ?: emptyList()
        state = state.copy(
            passwordItem = state.passwordItem?.copy(images = current + image),
            isChanged = true
        )
    }

    private fun onRemoveImage(index: Int) {
        val current = state.passwordItem?.images ?: return
        if (index !in current.indices) return
        val updated = current.toMutableList().apply { removeAt(index) }
        state = state.copy(
            passwordItem = state.passwordItem?.copy(images = updated),
            isChanged = true
        )
    }

    private fun onSelectCategory(category: CategoryModel?) {
        state = if (category == null) {
            state.copy(category = noCategoryModel, isChanged = true)
        } else {
            state.copy(category = category, isChanged = true)
        }
    }

    private fun onRequestExportAttachment(event: EditPasswordItemUiEvent.RequestExportAttachment) {
        state = state.copy(
            isExportAttachmentDialogVisible = true,
            attachmentToExport = AttachmentToExport(event.bytes, event.fileName, event.mimeType)
        )
    }

    private suspend fun onExportAttachment(event: EditPasswordItemUiEvent.ExportAttachment): EditPasswordItemUiEffect? {
        val isValid = userPreferencesRepository.verifyPassword(event.password)
        return if (isValid) {
            val attachment = state.attachmentToExport ?: return null
            state = state.copy(isExportAttachmentDialogVisible = false, isExportAttachmentPasswordInvalid = false)
            EditPasswordItemUiEffect.OpenExportAttachmentIntent(attachment.fileName, attachment.mimeType)
        } else {
            state = state.copy(isExportAttachmentPasswordInvalid = true)
            null
        }
    }

    private fun onToggleExportAttachmentDialogVisibility() {
        state = state.copy(
            isExportAttachmentDialogVisible = !state.isExportAttachmentDialogVisible,
            isExportAttachmentPasswordInvalid = false
        )
    }

    override fun onEvent(event: EditPasswordItemUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is EditPasswordItemUiEvent.EditPassword -> editPassword()
                is EditPasswordItemUiEvent.ToggleUnsavedChangesDialogVisibility -> toggleVisibility(event)
                is EditPasswordItemUiEvent.ToggleAlreadyAutoFocused -> toggleVisibility(event)
                is EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility -> toggleVisibility(event)
                is EditPasswordItemUiEvent.ToggleShowPassword -> toggleVisibility(event)
                is EditPasswordItemUiEvent.GenerateRandomPassword -> onGenerateRandomPassword()
                is EditPasswordItemUiEvent.EnterName -> onEnterText(event)
                is EditPasswordItemUiEvent.EnterUsername -> onEnterText(event)
                is EditPasswordItemUiEvent.EnterPassword -> onEnterText(event)
                is EditPasswordItemUiEvent.EnterWebsite -> onEnterText(event)
                is EditPasswordItemUiEvent.EnterNotes -> onEnterText(event)
                is EditPasswordItemUiEvent.AddImage -> onAddImage(event.image)
                is EditPasswordItemUiEvent.RemoveImage -> onRemoveImage(event.index)
                is EditPasswordItemUiEvent.SelectCategory -> onSelectCategory(event.category)
                is EditPasswordItemUiEvent.NavigateToAddCategory -> EditPasswordItemUiEffect.NavigateToAddCategory
                is EditPasswordItemUiEvent.NavigateUp -> EditPasswordItemUiEffect.NavigateUp
                is EditPasswordItemUiEvent.RequestExportAttachment -> onRequestExportAttachment(event)
                is EditPasswordItemUiEvent.ExportAttachment -> onExportAttachment(event)
                is EditPasswordItemUiEvent.ToggleExportAttachmentDialogVisibility -> onToggleExportAttachmentDialogVisibility()
            }

            if (effect is EditPasswordItemUiEffect) _effectChannel.send(effect)
        }
    }
}
