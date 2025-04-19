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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
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
    val passwordItem = passwordItemRepository.getPasswordItem(passwordItemDetail.id)

    private val _effectChannel = Channel<PasswordItemDetailUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            state = state.copy(
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

    private suspend fun toggleAddedToWatch(): PasswordItemDetailUiEffect? {
        val passwordWithCategoryModel = passwordItem.first()
        if (passwordWithCategoryModel == null) return null

        passwordItemRepository.insertPasswordItem(
            PasswordItemModel(
                id = passwordWithCategoryModel.id,
                name = passwordWithCategoryModel.name,
                username = passwordWithCategoryModel.username,
                password = passwordWithCategoryModel.password,
                notes = passwordWithCategoryModel.notes,
                categoryId = passwordWithCategoryModel.categoryId,
                website = passwordWithCategoryModel.website,
                isAddedToWatch = !passwordWithCategoryModel.isAddedToWatch,
                createdAt = passwordWithCategoryModel.createdAt
            )
        )

        return PasswordItemDetailUiEffect.ToggleAddedToWatch
    }

    private suspend fun deleteItem(): PasswordItemDetailUiEffect? {
        val passwordWithCategoryModel = passwordItem.first()
        if (passwordWithCategoryModel == null) return null

        passwordItemRepository.deletePasswordItem(passwordWithCategoryModel)
        return PasswordItemDetailUiEffect.DeleteItem
    }

    override fun onEvent(event: PasswordItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility -> toggleVisibility(event)
                is PasswordItemDetailUiEvent.ToggleShowPasswordVisibility -> toggleVisibility(event)
                is PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility -> toggleVisibility(event)
                is PasswordItemDetailUiEvent.DeleteItem -> deleteItem()
                is PasswordItemDetailUiEvent.ToggleAddedToWatch -> toggleAddedToWatch()
                is PasswordItemDetailUiEvent.CopyText -> PasswordItemDetailUiEffect.CopyText(event.text)
                is PasswordItemDetailUiEvent.LaunchUrl -> PasswordItemDetailUiEffect.LaunchUrl(event.url)
                is PasswordItemDetailUiEvent.NavigateToEditPassword -> PasswordItemDetailUiEffect.NavigateToEditPassword(event.id)
                is PasswordItemDetailUiEvent.NavigateUp -> PasswordItemDetailUiEffect.NavigateUp
            }

            if (effect is PasswordItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
