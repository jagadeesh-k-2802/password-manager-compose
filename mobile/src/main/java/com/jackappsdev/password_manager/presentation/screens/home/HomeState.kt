package com.jackappsdev.password_manager.presentation.screens.home

import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.model.SortBy
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import kotlinx.coroutines.flow.StateFlow

data class HomeState(
    val items: StateFlow<List<PasswordItemModel>>? = null,
    val categoryItems: StateFlow<List<CategoryModel>>? = null,
    val isLoading: Boolean = true,
    val sortBy: SortBy = SortBy.ALPHABET_ASCENDING,
    val filterBy: FilterBy = FilterBy.All,
    val filteredItems: StateFlow<List<PasswordItemModel>>? = null,
    val searchQuery: String = EMPTY_STRING,
    val isSearching: Boolean = false,
)
