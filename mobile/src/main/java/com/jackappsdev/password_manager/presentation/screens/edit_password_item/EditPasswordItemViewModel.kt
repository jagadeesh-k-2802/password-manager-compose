package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.domain.mappers.toCategoryModel
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPasswordItemViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val passwordItemRepository: PasswordItemRepository,
    categoryRepository: CategoryRepository
) : ViewModel(), EventDrivenViewModel<EditPasswordItemUiEvent, EditPasswordItemUiEffect> {

    var state by mutableStateOf(EditPasswordItemState())
        private set

    private val editPasswordItem = savedStateHandle.toRoute<Routes.EditPasswordItem>()

    private val _errorChannel = Channel<EditPasswordItemError>()
    val errorFlow = _errorChannel.receiveAsFlow()

    private val passwordItem = passwordItemRepository.getPasswordItem(editPasswordItem.id)
    val categoryItems = categoryRepository.getAllCategories()

    private val _effectChannel = Channel<EditPasswordItemUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val noCategoryModel = CategoryModel(
        name = application.getString(R.string.text_no_category),
        color = "#000000",
    )

    init {
        updateStateOnPasswordItemChange()
    }

    private fun updateStateOnPasswordItemChange() {
        viewModelScope.launch {
            passwordItem.collect { item ->
                state = state.copy(passwordItem = item, category = item?.toCategoryModel())
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
                isAddedToWatch = false,
                createdAt = System.currentTimeMillis()
            )

            passwordItemRepository.insertPasswordItem(newPasswordItemModel)
            return EditPasswordItemUiEffect.OnEditComplete
        }
    }

    private fun toggleCategoryDropdownVisibility() {
        state = state.copy(isCategoryDropdownVisible = !state.isCategoryDropdownVisible)
    }

    private fun toggleIsAlreadyAutoFocused() {
        state = state.copy(isAlreadyAutoFocused = !state.isAlreadyAutoFocused)
    }

    private fun toggleShowPassword() {
        state = state.copy(showPassword = !state.showPassword)
    }

    private fun toggleUnsavedChangesDialogVisibility() {
        state = state.copy(isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible)
    }

    private fun onGenerateRandomPassword() {
        val password = generateRandomPassword(GeneratePasswordConfig(length = 12))
        state = state.copy(passwordItem = state.passwordItem?.copy(password = password), isChanged = true)
    }

    private fun onEnterText(event: EditPasswordItemUiEvent) {
        val newPasswordItem = when (event) {
            is EditPasswordItemUiEvent.OnEnterName -> state.passwordItem?.copy(name = event.text)
            is EditPasswordItemUiEvent.OnEnterUsername -> state.passwordItem?.copy(username = event.text)
            is EditPasswordItemUiEvent.OnEnterPassword -> state.passwordItem?.copy(password = event.text)
            is EditPasswordItemUiEvent.OnEnterWebsite -> state.passwordItem?.copy(website = event.text)
            is EditPasswordItemUiEvent.OnEnterNotes -> state.passwordItem?.copy(notes = event.text)
            else -> null
        }

        state = state.copy(passwordItem = newPasswordItem, isChanged = true)
    }

    private fun onSelectCategory(category: CategoryModel?) {
        state = if (category == null) {
            state.copy(category = noCategoryModel, isChanged = true)
        } else {
            state.copy(category = category, isChanged = true)
        }
    }

    override fun onEvent(event: EditPasswordItemUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is EditPasswordItemUiEvent.EditPassword -> editPassword()
                is EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility -> toggleCategoryDropdownVisibility()
                is EditPasswordItemUiEvent.ToggleIsAlreadyAutoFocused -> toggleIsAlreadyAutoFocused()
                is EditPasswordItemUiEvent.ToggleShowPassword -> toggleShowPassword()
                is EditPasswordItemUiEvent.ToggleUnsavedChangesDialogVisibility -> toggleUnsavedChangesDialogVisibility()
                is EditPasswordItemUiEvent.OnGenerateRandomPassword -> onGenerateRandomPassword()
                is EditPasswordItemUiEvent.OnEnterName -> onEnterText(event)
                is EditPasswordItemUiEvent.OnEnterNotes -> onEnterText(event)
                is EditPasswordItemUiEvent.OnEnterPassword -> onEnterText(event)
                is EditPasswordItemUiEvent.OnEnterUsername -> onEnterText(event)
                is EditPasswordItemUiEvent.OnEnterWebsite -> onEnterText(event)
                is EditPasswordItemUiEvent.OnSelectCategory -> onSelectCategory(event.category)
                is EditPasswordItemUiEvent.NavigateToAddCategory -> EditPasswordItemUiEffect.NavigateToAddCategory
                is EditPasswordItemUiEvent.NavigateUp -> EditPasswordItemUiEffect.NavigateUp
            }

            if (effect is EditPasswordItemUiEffect) _effectChannel.send(effect)
        }
    }
}
