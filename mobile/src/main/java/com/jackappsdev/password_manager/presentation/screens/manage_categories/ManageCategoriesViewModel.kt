package com.jackappsdev.password_manager.presentation.screens.manage_categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesUiEffect
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel(), EventDrivenViewModel<ManageCategoriesUiEvent, ManageCategoriesUiEffect> {

    var state by mutableStateOf(ManageCategoriesState())
        private set

    private val _effectChannel = Channel<ManageCategoriesUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        if (state.isLoading) {
            viewModelScope.launch {
                val categoryFlow = categoryRepository.getAllCategories().stateIn(viewModelScope)
                state = state.copy(items = categoryFlow, isLoading = false)
            }
        }
    }

    override fun onEvent(event: ManageCategoriesUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is ManageCategoriesUiEvent.ScrollToTop -> ManageCategoriesUiEffect.ScrollToTop
                is ManageCategoriesUiEvent.NavigateToCategoryItem -> ManageCategoriesUiEffect.NavigateToCategoryItem(event.id)
                is ManageCategoriesUiEvent.NavigateToAddCategory -> ManageCategoriesUiEffect.NavigateToAddCategory
                is ManageCategoriesUiEvent.NavigateUp -> ManageCategoriesUiEffect.NavigateUp
            }

            _effectChannel.send(effect)
        }
    }
}
