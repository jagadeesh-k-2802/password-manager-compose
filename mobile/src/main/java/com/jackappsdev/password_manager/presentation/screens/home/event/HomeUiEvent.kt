package com.jackappsdev.password_manager.presentation.screens.home.event

import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.SortBy

sealed class HomeUiEvent {
    data object LockApplication : HomeUiEvent()
    data class EnterSearchQuery(val query: String): HomeUiEvent()
    data object ClearSearch : HomeUiEvent()
    data object SearchItems : HomeUiEvent()
    data class SelectFilterBy(val filterBy: FilterBy) : HomeUiEvent()
    data class SelectSortBy(val sortBy: SortBy) : HomeUiEvent()
    data object Search : HomeUiEvent()
    data object ScrollToTop : HomeUiEvent()
    data object ToggleSortSheetVisibility : HomeUiEvent()
    data object ToggleFilterSheetVisibility : HomeUiEvent()
    data class NavigateToPasswordItem(val id: Int) : HomeUiEvent()
    data object NavigateToAddPassword : HomeUiEvent()
}
