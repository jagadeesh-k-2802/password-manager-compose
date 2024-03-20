package com.jagadeesh.passwordmanager.presentation.screens.home

import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.StateFlow

data class HomeState(
    val sortBy: SortBy = SortBy.ALPHABET_ASCENDING,
    val items: StateFlow<List<PasswordItemModel>>? = null,
    val filteredItems: StateFlow<List<PasswordItemModel>>? = null,
    val isLoading: Boolean = true
)
