package com.jagadeesh.passwordmanager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.repository.CategoryRepository
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    val categoryItems = categoryRepository.getAllCategories()

    var state by mutableStateOf(HomeState())
        private set

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            if (state.isLoading) {
                state = state.copy(
                    isLoading = false,
                    items = passwordItemRepository.getPasswordItems(state.sortBy.orderBy(), "")
                        .stateIn(viewModelScope)
                )
            }
        }
    }

    fun setSortBy(sortBy: SortBy) {
        viewModelScope.launch {
            state = state.copy(
                sortBy = sortBy,
                items = passwordItemRepository.getPasswordItems(sortBy.orderBy(), "")
                    .stateIn(viewModelScope)
            )
        }
    }

    fun searchItems(searchQuery: String) {
        viewModelScope.launch {
            state = if (searchQuery.isEmpty()) {
                state.copy(filteredItems = null)
            } else {
                state.copy(
                    filteredItems = passwordItemRepository.getPasswordItems(
                        state.sortBy.orderBy(),
                        searchQuery
                    ).stateIn(viewModelScope)
                )
            }
        }
    }
}
