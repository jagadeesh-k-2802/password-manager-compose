package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryItemViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel(), EventDrivenViewModel<AddCategoryItemUiEvent, AddCategoryItemUiEffect> {

    var state by mutableStateOf(AddCategoryItemState())
        private set

    private val _effectChannel = Channel<AddCategoryItemUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val _errorChannel = Channel<AddCategoryItemError>()
    val errorFlow = Channel<AddCategoryItemError>().receiveAsFlow()

    private suspend fun addCategoryItem(): AddCategoryItemUiEffect? {
        return if (state.name.isEmpty()) {
            _errorChannel.send(AddCategoryItemError.NameError(R.string.error_name_not_empty))
            null
        } else {
            val model = CategoryModel(name = state.name, color = state.color)
            val id = categoryRepository.insertCategoryItem(model)
            AddCategoryItemUiEffect.NavigateUp(model.copy(id = id.toInt()))
        }
    }

    private fun toggleUnsavedChangesDialog() {
        state = state.copy(isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible)
    }

    private fun onEnterName(name: String) {
        state = state.copy(name = name)
    }

    private fun onSelectColor(color: String) {
        state = state.copy(color = color)
    }

    override fun onEvent(event: AddCategoryItemUiEvent) {
        viewModelScope.launch {
            val effect = when(event) {
                is AddCategoryItemUiEvent.AddCategoryItem -> addCategoryItem()
                is AddCategoryItemUiEvent.ToggleUnsavedChangesDialog -> toggleUnsavedChangesDialog()
                is AddCategoryItemUiEvent.OnEnterName -> onEnterName(event.name)
                is AddCategoryItemUiEvent.OnSelectColor -> onSelectColor(event.color)
            }

            if (effect is AddCategoryItemUiEffect) _effectChannel.send(effect)
        }
    }
}
