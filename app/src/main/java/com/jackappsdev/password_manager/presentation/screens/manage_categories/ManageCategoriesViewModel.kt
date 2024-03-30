package com.jackappsdev.password_manager.presentation.screens.manage_categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    var state by mutableStateOf(ManageCategoriesState())
        private set

    init {
        onInit()
    }

    private fun onInit() {
        if (state.isLoading) {
            viewModelScope.launch {
                state = state.copy(
                    items = categoryRepository.getAllCategories().stateIn(viewModelScope),
                    isLoading = false
                )
            }
        }
    }
}
