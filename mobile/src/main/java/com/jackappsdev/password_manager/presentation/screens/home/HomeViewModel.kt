package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.SortBy
import com.jackappsdev.password_manager.domain.model.orderBy
import com.jackappsdev.password_manager.domain.model.where
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

    fun setSortBy(sortBy: SortBy, searchQuery: String) {
        viewModelScope.launch {
            state = state.copy(
                sortBy = sortBy,
                items = passwordItemRepository.getPasswordItems(
                    sortBy.orderBy(),
                    state.filterBy.where(),
                    ""
                ).stateIn(viewModelScope)
            )

            if (searchQuery.isNotEmpty()) {
                searchItems(searchQuery)
            }
        }
    }

    fun filterByCategory(filterBy: FilterBy, searchQuery: String) {
        viewModelScope.launch {
            state = state.copy(
                filterBy = filterBy,
                items = passwordItemRepository.getPasswordItems(
                    state.sortBy.orderBy(),
                    filterBy.where(),
                    ""
                ).stateIn(viewModelScope)
            )

            if (searchQuery.isNotEmpty()) {
                searchItems(searchQuery)
            }
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
                        searchQuery.trim()
                    ).stateIn(viewModelScope)
                )
            }
        }
    }

    fun lockApplication() {
        Runtime.getRuntime().exit(0)
    }
}
