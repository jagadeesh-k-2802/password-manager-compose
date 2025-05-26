package com.jackappsdev.password_manager.presentation.screens.category_item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository
) : ViewModel(), EventDrivenViewModel<CategoryItemDetailUiEvent, CategoryItemDetailUiEffect> {

    var state by mutableStateOf(CategoryItemDetailState())
        private set

    private val categoryItemDetail = savedStateHandle.toRoute<Routes.CategoryItemDetail>()
    private val categoryItem = categoryRepository.getCategoryItem(categoryItemDetail.id).filterNotNull()

    private val _effectChannel = Channel<CategoryItemDetailUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            categoryItem.collectLatest { item ->
                state = state.copy(categoryModel = item)
            }
        }
    }

    private fun toggleVisibility(event: CategoryItemDetailUiEvent) {
        when (event) {
            is CategoryItemDetailUiEvent.ToggleCategoryItemDeleteDialogVisibility -> {
                state = state.copy(isDeleteDialogVisible = !state.isDeleteDialogVisible)
            }

            is CategoryItemDetailUiEvent.ToggleUnsavedChangesDialogVisibility -> {
                state = state.copy(isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible)
            }

            else -> {
                null
            }
        }
    }

    private suspend fun updateCategoryItem(): CategoryItemDetailUiEffect {
        categoryRepository.insertCategoryItem(
            CategoryModel(
                id = state.categoryModel?.id,
                name = state.categoryModel?.name ?: EMPTY_STRING,
                color = state.categoryModel?.color ?: EMPTY_STRING,
                createdAt = System.currentTimeMillis()
            )
        )

        return CategoryItemDetailUiEffect.NavigateUp
    }

    private suspend fun deleteItem(): CategoryItemDetailUiEffect {
        state = state.copy(isDeleteDialogVisible = false)
        state.categoryModel?.let { categoryRepository.deleteCategoryItem(it) }
        return CategoryItemDetailUiEffect.NavigateUp
    }

    private fun onEnterName(name: String) {
        state.categoryModel?.let {
            state = state.copy(
                categoryModel = it.copy(name = name),
                isChanged = true
            )
        }
    }

    private fun onSelectColor(color: String) {
        state.categoryModel?.let {
            state = state.copy(
                categoryModel = it.copy(color = color),
                isChanged = true
            )
        }
    }

    override fun onEvent(event: CategoryItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is CategoryItemDetailUiEvent.ToggleCategoryItemDeleteDialogVisibility -> toggleVisibility(event)
                is CategoryItemDetailUiEvent.ToggleUnsavedChangesDialogVisibility -> toggleVisibility(event)
                is CategoryItemDetailUiEvent.UpdateCategoryItem -> updateCategoryItem()
                is CategoryItemDetailUiEvent.DeleteCategoryItem -> deleteItem()
                is CategoryItemDetailUiEvent.EnterName -> onEnterName(event.name)
                is CategoryItemDetailUiEvent.SelectColor -> onSelectColor(event.color)
                is CategoryItemDetailUiEvent.NavigateUp -> CategoryItemDetailUiEffect.NavigateUp
            }

            if (effect is CategoryItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
