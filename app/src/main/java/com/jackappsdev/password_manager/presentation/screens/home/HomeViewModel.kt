package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
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
                    items = passwordItemRepository.getPasswordItems(
                        state.sortBy.orderBy(),
                        state.filterBy.where(),
                        ""
                    )
                        .stateIn(viewModelScope)
                )
            }
        }
    }

    fun setSortBy(sortBy: SortBy) {
        viewModelScope.launch {
            state = state.copy(
                sortBy = sortBy,
                items = passwordItemRepository.getPasswordItems(
                    sortBy.orderBy(),
                    state.filterBy.where(),
                    ""
                )
                    .stateIn(viewModelScope)
            )
        }
    }

    fun filterByCategory(filterBy: FilterBy) {
        viewModelScope.launch {
            state = state.copy(
                filterBy = filterBy,
                items = passwordItemRepository.getPasswordItems(
                    state.sortBy.orderBy(),
                    filterBy.where(),
                    ""
                )
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
                        state.filterBy.where(),
                        searchQuery
                    ).stateIn(viewModelScope)
                )
            }
        }
    }
}
