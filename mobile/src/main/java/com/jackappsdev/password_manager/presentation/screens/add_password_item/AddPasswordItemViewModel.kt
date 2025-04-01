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
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPasswordItemViewModel @Inject constructor(
    application: Application,
    private val passwordItemRepository: PasswordItemRepository,
    categoryRepository: CategoryRepository
) : ViewModel(), EventDrivenViewModel<AddPasswordItemUiEvent, AddPasswordItemUiEffect> {

    var state by mutableStateOf(AddPasswordItemState())
        private set

    private val _effectChannel = Channel<AddPasswordItemUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<AddPasswordItemError>()
    val errorFlow = _errorChannel.receiveAsFlow()

    val categoryItems = categoryRepository.getAllCategories()

    private val noCategoryModel = CategoryModel(
        name = application.getString(R.string.text_no_category),
        color = "#000000",
    )

    init {
        initializeData()
    }

    private fun initializeData() {
        state = state.copy(category = noCategoryModel)
    }

    private fun onEnterText(event: AddPasswordItemUiEvent) {
        when (event) {
            is AddPasswordItemUiEvent.OnEnterName -> state = state.copy(name = event.name)
            is AddPasswordItemUiEvent.OnEnterUsername -> state = state.copy(username = event.username)
            is AddPasswordItemUiEvent.OnEnterPassword -> state = state.copy(password = event.password)
            is AddPasswordItemUiEvent.OnEnterNotes -> state = state.copy(notes = event.notes)
            is AddPasswordItemUiEvent.OnEnterWebsite -> state = state.copy(website = event.website)
            else -> null
        }

        checkIfUserEnteredDetails()
    }

    private suspend fun addPasswordItem(): AddPasswordItemUiEffect? {
        return if (state.name.isEmpty()) {
            _errorChannel.send(AddPasswordItemError.NameError(R.string.error_name_not_empty))
            null
        } else {
            val passwordItemModel = with(state) {
                PasswordItemModel(
                    name = name,
                    username = username,
                    password = password,
                    website = website,
                    notes = notes,
                    categoryId = category?.id,
                    isAddedToWatch = false
                )
            }

            passwordItemRepository.insertPasswordItem(passwordItemModel)
            AddPasswordItemUiEffect.NavigateUp
        }
    }

    private fun onGenerateRandomPassword() {
        val password = generateRandomPassword(GeneratePasswordConfig(length = 12))
        state = state.copy(password = password)
    }

    private fun toggleShowPassword() {
        state = state.copy(showPassword = !state.showPassword)
    }

    private fun toggleIsCategoryDropdownVisible() {
        state = state.copy(isCategoryDropdownVisible = !state.isCategoryDropdownVisible)
    }

    private fun toggleIsUnsavedDialogVisible() {
        state = state.copy(isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible)
    }

    private fun setIsAlreadyAutoFocus() {
        state = state.copy(isAlreadyAutoFocused = true)
    }

    private fun onSetCategory(category: CategoryModel?) {
        state = if (category == null) {
            state.copy(category = noCategoryModel)
        } else {
            state.copy(category = category)
        }
    }

    private fun checkIfUserEnteredDetails() {
        state = state.copy(
            hasUserEnteredDetails = state.name.isNotBlank() ||
                state.username.isNotBlank() ||
                state.password.isNotBlank() ||
                state.notes.isNotBlank()
        )
    }

    override fun onEvent(event: AddPasswordItemUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is AddPasswordItemUiEvent.OnEnterName -> onEnterText(event)
                is AddPasswordItemUiEvent.OnEnterNotes -> onEnterText(event)
                is AddPasswordItemUiEvent.OnEnterPassword -> onEnterText(event)
                is AddPasswordItemUiEvent.OnEnterUsername -> onEnterText(event)
                is AddPasswordItemUiEvent.OnEnterWebsite -> onEnterText(event)
                is AddPasswordItemUiEvent.AddPasswordItem -> addPasswordItem()
                is AddPasswordItemUiEvent.OnGenerateRandomPassword -> onGenerateRandomPassword()
                is AddPasswordItemUiEvent.ToggleShowPassword -> toggleShowPassword()
                is AddPasswordItemUiEvent.ToggleIsCategoryDropdownVisible -> toggleIsCategoryDropdownVisible()
                is AddPasswordItemUiEvent.ToggleIsUnsavedDialogVisible -> toggleIsUnsavedDialogVisible()
                is AddPasswordItemUiEvent.SetIsAlreadyAutoFocus -> setIsAlreadyAutoFocus()
                is AddPasswordItemUiEvent.OnSelectCategory -> onSetCategory(event.category)
            }

            if (effect is AddPasswordItemUiEffect) _effectChannel.send(effect)
        }
    }
}
