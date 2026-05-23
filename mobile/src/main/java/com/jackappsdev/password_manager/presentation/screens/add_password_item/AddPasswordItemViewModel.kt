package com.jackappsdev.password_manager.presentation.screens.add_password_item

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.model.AttachmentToExport
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPasswordItemViewModel @Inject constructor(
    application: Application,
    private val passwordItemRepository: PasswordItemRepository,
    private val categoryRepository: CategoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel(), EventDrivenViewModel<AddPasswordItemUiEvent, AddPasswordItemUiEffect> {

    var state by mutableStateOf(AddPasswordItemState())
        private set

    private val noCategoryModel = CategoryModel(name = application.getString(R.string.text_no_category), color = "#000000")

    private val _effectChannel = Channel<AddPasswordItemUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<AddPasswordItemError>()
    val errorFlow = _errorChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            val categoryItems = categoryRepository.getAllCategories()

            state = state.copy(
                categoryItems = categoryItems.stateIn(viewModelScope),
                category = noCategoryModel
            )
        }
    }

    private fun onEnterText(event: AddPasswordItemUiEvent) {
        when (event) {
            is AddPasswordItemUiEvent.EnterName -> state = state.copy(name = event.name)
            is AddPasswordItemUiEvent.EnterUsername -> state = state.copy(username = event.username)
            is AddPasswordItemUiEvent.EnterPassword -> state = state.copy(password = event.password)
            is AddPasswordItemUiEvent.EnterNotes -> state = state.copy(notes = event.notes)
            is AddPasswordItemUiEvent.EnterWebsite -> state = state.copy(website = event.website)
            else -> null
        }

        checkIfUserEnteredDetails()
    }

    private fun checkIfUserEnteredDetails() {
        state = state.copy(
            hasUserEnteredDetails = state.name.isNotBlank() ||
                state.username.isNotBlank() ||
                state.password.isNotBlank() ||
                state.notes.isNotBlank() ||
                state.images.isNotEmpty()
        )
    }

    private fun onAddImage(image: ByteArray) {
        state = state.copy(images = state.images + image)
        checkIfUserEnteredDetails()
    }

    private fun onRemoveImage(index: Int) {
        if (index !in state.images.indices) return
        state = state.copy(images = state.images.toMutableList().apply { removeAt(index) })
        checkIfUserEnteredDetails()
    }

    private fun onGenerateRandomPassword() {
        val password = generateRandomPassword(GeneratePasswordConfig(length = 13))
        state = state.copy(password = password)
    }

    private fun toggleVisibility(event: AddPasswordItemUiEvent) {
        when (event) {
            is AddPasswordItemUiEvent.ToggleAlreadyAutoFocusVisibility -> {
                state = state.copy(isAlreadyAutoFocused = !state.isAlreadyAutoFocused)
            }

            is AddPasswordItemUiEvent.ToggleShowPasswordVisibility -> {
                state = state.copy(showPassword = !state.showPassword)
            }

            is AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility -> {
                state = state.copy(isCategoryDropdownVisible = !state.isCategoryDropdownVisible)
            }

            is AddPasswordItemUiEvent.ToggleUnsavedDialogVisibility -> {
                state = state.copy(isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible)
            }

            else -> {
                null
            }
        }
    }

    private fun onSelectCategory(category: CategoryModel?) {
        state = if (category == null) {
            state.copy(category = noCategoryModel)
        } else {
            state.copy(category = category)
        }
    }

    private suspend fun addPasswordItem(): AddPasswordItemUiEffect? {
        return if (state.name.isEmpty()) {
            _errorChannel.send(AddPasswordItemError.NameError(R.string.error_name_not_empty))
            null
        } else {
            val isWatchEnabled = userPreferencesRepository.hasAndroidWatchPinSet()
            val autoAdd = userPreferencesRepository.getAutoAddPasswordsToWatch().first()
            val isAddedToWatch = isWatchEnabled && autoAdd

            val passwordItemModel = with(state) {
                PasswordItemModel(
                    name = name,
                    username = username,
                    password = password,
                    website = website,
                    notes = notes,
                    categoryId = category?.id,
                    images = images,
                    isAddedToWatch = isAddedToWatch
                )
            }

            val generatedId = passwordItemRepository.upsertPasswordItem(passwordItemModel)

            val createdPasswordWithCategory = PasswordWithCategoryModel(
                id = generatedId,
                name = passwordItemModel.name,
                username = passwordItemModel.username,
                password = passwordItemModel.password,
                notes = passwordItemModel.notes,
                website = passwordItemModel.website,
                isAddedToWatch = passwordItemModel.isAddedToWatch,
                categoryId = passwordItemModel.categoryId,
                categoryName = if (state.category?.name == noCategoryModel.name) null else state.category?.name,
                categoryColor = if (state.category?.name == noCategoryModel.name) null else state.category?.color,
                images = passwordItemModel.images,
                createdAt = passwordItemModel.createdAt
            )

            AddPasswordItemUiEffect.NavigateUp(createdPasswordWithCategory)
        }
    }

    private fun onRequestExportAttachment(event: AddPasswordItemUiEvent.RequestExportAttachment) {
        state = state.copy(
            isExportAttachmentDialogVisible = true,
            attachmentToExport = AttachmentToExport(event.bytes, event.fileName, event.mimeType)
        )
    }

    private suspend fun onExportAttachment(event: AddPasswordItemUiEvent.ExportAttachment): AddPasswordItemUiEffect? {
        val isValid = userPreferencesRepository.verifyPassword(event.password)
        return if (isValid) {
            val attachment = state.attachmentToExport ?: return null
            state = state.copy(isExportAttachmentDialogVisible = false, isExportAttachmentPasswordInvalid = false)
            AddPasswordItemUiEffect.OpenExportAttachmentIntent(attachment.fileName, attachment.mimeType)
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

    override fun onEvent(event: AddPasswordItemUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is AddPasswordItemUiEvent.EnterName -> onEnterText(event)
                is AddPasswordItemUiEvent.EnterUsername -> onEnterText(event)
                is AddPasswordItemUiEvent.EnterPassword -> onEnterText(event)
                is AddPasswordItemUiEvent.EnterWebsite -> onEnterText(event)
                is AddPasswordItemUiEvent.EnterNotes -> onEnterText(event)
                is AddPasswordItemUiEvent.AddImage -> onAddImage(event.image)
                is AddPasswordItemUiEvent.RemoveImage -> onRemoveImage(event.index)
                is AddPasswordItemUiEvent.GenerateRandomPassword -> onGenerateRandomPassword()
                is AddPasswordItemUiEvent.ToggleAlreadyAutoFocusVisibility -> toggleVisibility(event)
                is AddPasswordItemUiEvent.ToggleShowPasswordVisibility -> toggleVisibility(event)
                is AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility -> toggleVisibility(event)
                is AddPasswordItemUiEvent.ToggleUnsavedDialogVisibility -> toggleVisibility(event)
                is AddPasswordItemUiEvent.SelectCategory -> onSelectCategory(event.category)
                is AddPasswordItemUiEvent.AddPasswordItem -> addPasswordItem()
                is AddPasswordItemUiEvent.NavigateToAddCategory -> AddPasswordItemUiEffect.NavigateToAddCategory
                is AddPasswordItemUiEvent.NavigateUp -> AddPasswordItemUiEffect.NavigateUp()
                is AddPasswordItemUiEvent.RequestExportAttachment -> onRequestExportAttachment(event)
                is AddPasswordItemUiEvent.ExportAttachment -> onExportAttachment(event)
                is AddPasswordItemUiEvent.ToggleExportAttachmentDialogVisibility -> onToggleExportAttachmentDialogVisibility()
            }

            if (effect is AddPasswordItemUiEffect) _effectChannel.send(effect)
        }
    }
}
