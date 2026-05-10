package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.model.AttachmentToExport
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PasswordItemDetailViewModel.Factory::class)
class PasswordItemDetailViewModel @AssistedInject constructor(
    @Assisted val passwordItemDetail: Routes.PasswordItemDetail,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel(), EventDrivenViewModel<PasswordItemDetailUiEvent, PasswordItemDetailUiEffect> {

    @AssistedFactory
    interface Factory {
        fun create(passwordItemDetail: Routes.PasswordItemDetail): PasswordItemDetailViewModel
    }

    var state by mutableStateOf(PasswordItemDetailState())
        private set

    private val _effectChannel = Channel<PasswordItemDetailUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            val passwordItem = passwordItemRepository.getPasswordItem(passwordItemDetail.id)

            state = state.copy(
                passwordItem = passwordItem.stateIn(viewModelScope),
                hasAndroidWatchPinSet = userPreferencesRepository.hasAndroidWatchPinSet(),
            )
        }
    }

    private fun toggleVisibility(event: PasswordItemDetailUiEvent) {
        when (event) {
            is PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility -> {
                state = state.copy(isDeleteDialogVisible = !state.isDeleteDialogVisible)
            }

            is PasswordItemDetailUiEvent.ToggleShowPasswordVisibility -> {
                state = state.copy(showPassword = !state.showPassword)
            }

            is PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility -> {
                state = state.copy(dropDownMenuExpanded = !state.dropDownMenuExpanded)
            }

            else -> {
                // No Operation
            }
        }
    }

    private suspend fun toggleAddedToWatch() {
        val passwordWithCategoryModel = state.passwordItem?.value ?: return

        passwordItemRepository.upsertPasswordItem(
            PasswordItemModel(
                id = passwordWithCategoryModel.id,
                name = passwordWithCategoryModel.name,
                username = passwordWithCategoryModel.username,
                password = passwordWithCategoryModel.password,
                notes = passwordWithCategoryModel.notes,
                categoryId = passwordWithCategoryModel.categoryId,
                website = passwordWithCategoryModel.website,
                isAddedToWatch = passwordWithCategoryModel.isAddedToWatch.not(),
                images = passwordWithCategoryModel.images,
                createdAt = passwordWithCategoryModel.createdAt
            )
        )
    }

    private suspend fun deleteItem() {
        val passwordWithCategoryModel = state.passwordItem?.value ?: return
        passwordItemRepository.deletePasswordItem(passwordWithCategoryModel)
    }

    private fun onRequestExportAttachment(event: PasswordItemDetailUiEvent.RequestExportAttachment) {
        state = state.copy(
            isExportAttachmentDialogVisible = true,
            attachmentToExport = AttachmentToExport(event.bytes, event.fileName, event.mimeType)
        )
    }

    private suspend fun onExportAttachment(event: PasswordItemDetailUiEvent.ExportAttachment): PasswordItemDetailUiEffect? {
        val isValid = userPreferencesRepository.verifyPassword(event.password)
        return if (isValid) {
            val attachment = state.attachmentToExport ?: return null
            state = state.copy(isExportAttachmentDialogVisible = false, isExportAttachmentPasswordInvalid = false)
            PasswordItemDetailUiEffect.OpenExportAttachmentIntent(attachment.fileName, attachment.mimeType)
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

    override fun onEvent(event: PasswordItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility -> { toggleVisibility(event); null }
                is PasswordItemDetailUiEvent.ToggleShowPasswordVisibility -> { toggleVisibility(event); null }
                is PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility -> { toggleVisibility(event); null }
                is PasswordItemDetailUiEvent.DeleteItem -> { deleteItem(); null }
                is PasswordItemDetailUiEvent.ToggleAddToWatch -> { toggleAddedToWatch(); null }
                is PasswordItemDetailUiEvent.CopyText -> PasswordItemDetailUiEffect.CopyText(event.text)
                is PasswordItemDetailUiEvent.LaunchUrl -> PasswordItemDetailUiEffect.LaunchUrl(event.url)
                is PasswordItemDetailUiEvent.RequestToggleAddToWatch -> PasswordItemDetailUiEffect.ToggleAddToWatch
                is PasswordItemDetailUiEvent.RequestDeleteItem -> PasswordItemDetailUiEffect.DeleteItem
                is PasswordItemDetailUiEvent.NavigateToEditPassword -> PasswordItemDetailUiEffect.NavigateToEditPassword(event.id)
                is PasswordItemDetailUiEvent.NavigateUp -> PasswordItemDetailUiEffect.NavigateUp
                is PasswordItemDetailUiEvent.RequestExportAttachment -> onRequestExportAttachment(event)
                is PasswordItemDetailUiEvent.ExportAttachment -> onExportAttachment(event)
                is PasswordItemDetailUiEvent.ToggleExportAttachmentDialogVisibility -> onToggleExportAttachmentDialogVisibility()
            }

            if (effect is PasswordItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
