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
        updateStateOnCategoryItemChange()
    }

    private fun updateStateOnCategoryItemChange() {
        viewModelScope.launch {
            categoryItem.collectLatest { item ->
                state = state.copy(categoryModel = item)
            }
        }
    }

    private suspend fun updateCategoryItem(): CategoryItemDetailUiEffect {
        categoryRepository.insertCategoryItem(
            CategoryModel(
                id = state.categoryModel?.id,
                name = state.categoryModel?.name ?: "",
                color = state.categoryModel?.color ?: "",
                createdAt = System.currentTimeMillis()
            )
        )

        return CategoryItemDetailUiEffect.NavigateUp
    }

    private suspend fun deleteItem(): CategoryItemDetailUiEffect {
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

    private fun toggleCategoryItemDeleteDialog() {
        state = state.copy(
            isDeleteDialogVisible = !state.isDeleteDialogVisible
        )
    }

    private fun toggleUnsavedChangesDialog() {
        state = state.copy(
            isUnsavedChangesDialogVisible = !state.isUnsavedChangesDialogVisible
        )
    }

    override fun onEvent(event: CategoryItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is CategoryItemDetailUiEvent.UpdateCategoryItem -> updateCategoryItem()
                is CategoryItemDetailUiEvent.DeleteCategoryItem -> deleteItem()
                is CategoryItemDetailUiEvent.OnEnterName -> onEnterName(event.name)
                is CategoryItemDetailUiEvent.OnSelectColor -> onSelectColor(event.color)
                is CategoryItemDetailUiEvent.ToggleCategoryItemDeleteDialog -> toggleCategoryItemDeleteDialog()
                is CategoryItemDetailUiEvent.ToggleUnsavedChangesDialog -> toggleUnsavedChangesDialog()
            }

            if (effect is CategoryItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
