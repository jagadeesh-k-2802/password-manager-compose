package com.jackappsdev.password_manager.presentation.screens.home

import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.model.SortBy
import kotlinx.coroutines.flow.StateFlow

data class HomeState(
    val sortBy: SortBy = SortBy.ALPHABET_ASCENDING,
    val filterBy: FilterBy = FilterBy.All,
    val items: StateFlow<List<PasswordItemModel>>? = null,
    val filteredItems: StateFlow<List<PasswordItemModel>>? = null,
    val isLoading: Boolean = true
)
