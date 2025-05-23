package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel(), EventDrivenViewModel<PasswordItemDetailUiEvent, PasswordItemDetailUiEffect> {

    var state by mutableStateOf(PasswordItemDetailState())
        private set

    private val passwordItemDetail = savedStateHandle.toRoute<Routes.PasswordItemDetail>()

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
                null
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
                createdAt = passwordWithCategoryModel.createdAt
            )
        )
    }

    private suspend fun deleteItem() {
        val passwordWithCategoryModel = state.passwordItem?.value ?: return
        passwordItemRepository.deletePasswordItem(passwordWithCategoryModel)
    }

    override fun onEvent(event: PasswordItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility -> toggleVisibility(event)
                is PasswordItemDetailUiEvent.ToggleShowPasswordVisibility -> toggleVisibility(event)
                is PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility -> toggleVisibility(event)
                is PasswordItemDetailUiEvent.DeleteItem -> deleteItem()
                is PasswordItemDetailUiEvent.ToggleAddToWatch -> toggleAddedToWatch()
                is PasswordItemDetailUiEvent.CopyText -> PasswordItemDetailUiEffect.CopyText(event.text)
                is PasswordItemDetailUiEvent.LaunchUrl -> PasswordItemDetailUiEffect.LaunchUrl(event.url)
                is PasswordItemDetailUiEvent.RequestToggleAddToWatch -> PasswordItemDetailUiEffect.ToggleAddToWatch
                is PasswordItemDetailUiEvent.RequestDeleteItem -> PasswordItemDetailUiEffect.DeleteItem
                is PasswordItemDetailUiEvent.NavigateToEditPassword -> PasswordItemDetailUiEffect.NavigateToEditPassword(event.id)
                is PasswordItemDetailUiEvent.NavigateUp -> PasswordItemDetailUiEffect.NavigateUp
            }

            if (effect is PasswordItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
