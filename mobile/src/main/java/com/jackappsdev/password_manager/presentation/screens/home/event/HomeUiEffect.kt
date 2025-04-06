package com.jackappsdev.password_manager.presentation.screens.home.event

sealed class HomeUiEffect {
    data object ToggleFilterSheetVisibility : HomeUiEffect()
    data object ToggleSortSheetVisibility : HomeUiEffect()
    data object OnFilterSelected : HomeUiEffect()
    data object OnSortSelected : HomeUiEffect()
    data class NavigateToPasswordDetail(val id: Int) : HomeUiEffect()
    data object ScrollToTop : HomeUiEffect()
    data object OnSearch : HomeUiEffect()
    data object OnSearchCleared : HomeUiEffect()
    data object LockApplication : HomeUiEffect()
}
