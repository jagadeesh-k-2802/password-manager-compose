package com.jackappsdev.password_manager.presentation.screens.home.event

import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.SortBy

sealed class HomeUiEvent {
    data object LockApplication : HomeUiEvent()
    data class SelectSortBy(val sortBy: SortBy) : HomeUiEvent()
    data class SelectFilterBy(val filterBy: FilterBy) : HomeUiEvent()
    data class OnEnterSearchQuery(val query: String): HomeUiEvent()
    data object SearchItems : HomeUiEvent()
    data object OnSearch : HomeUiEvent()
    data object OnClearSearch : HomeUiEvent()
    data object ScrollToTop : HomeUiEvent()
    data class NavigateToPasswordDetail(val id: Int) : HomeUiEvent()
    data object ToggleSortSheetVisibility : HomeUiEvent()
    data object ToggleFilterSheetVisibility : HomeUiEvent()
}
