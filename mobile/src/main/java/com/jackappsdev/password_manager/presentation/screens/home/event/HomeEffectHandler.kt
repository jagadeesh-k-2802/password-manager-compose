package com.jackappsdev.password_manager.presentation.screens.home.event

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavHostController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class HomeEffectHandler(
    private val context: Context,
    private val navController: NavHostController,
    private val scope: CoroutineScope,
    private val filterBySheet: SheetState,
    private val sortBySheet: SheetState,
    private val lazyColumnState: LazyListState,
    private val keyboardController: SoftwareKeyboardController?,
    private val focusManager: FocusManager
) {

    fun onLockApplication() {
        context.showToast(context.getString(R.string.toast_app_locked))
    }

    fun onScrollToTop() {
        scope.launch { lazyColumnState.animateScrollToItem(0) }
    }

    fun onSearch() {
        keyboardController?.hide()
    }

    fun onSearchCleared() {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    fun onToggleFilterSheetVisibility() {
        scope.launch { filterBySheet.show() }
    }

    fun onToggleSortSheetVisibility() {
        scope.launch { sortBySheet.show() }
    }

    fun onFilterSelected(categoryItems: List<CategoryModel>?) {
        scope.launch {
            if (categoryItems?.isNotEmpty() == true) { lazyColumnState.animateScrollToItem(0) }
            filterBySheet.hide()
        }
    }

    fun onSortSelect(categoryItems: List<CategoryModel>?) {
        scope.launch {
            if (categoryItems?.isNotEmpty() == true) { lazyColumnState.animateScrollToItem(0) }
            sortBySheet.hide()
        }
    }

    fun onNavigateToPasswordItem(id: Int) {
        navController.navigate(Routes.PasswordItemDetail(id))
    }

    fun onNavigateToAddPassword() {
        navController.navigate(Routes.AddPasswordItem)
    }
}
