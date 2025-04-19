package com.jackappsdev.password_manager.presentation.screens.home.event

sealed class HomeUiEffect {
    data object LockApplication : HomeUiEffect()
    data object ScrollToTop : HomeUiEffect()
    data object Search : HomeUiEffect()
    data object SearchCleared : HomeUiEffect()
    data object ToggleFilterSheetVisibility : HomeUiEffect()
    data object ToggleSortSheetVisibility : HomeUiEffect()
    data object FilterSelected : HomeUiEffect()
    data object SortSelected : HomeUiEffect()
    data class NavigateToPasswordItem(val id: Int) : HomeUiEffect()
    data object NavigateToAddPassword : HomeUiEffect()
}
