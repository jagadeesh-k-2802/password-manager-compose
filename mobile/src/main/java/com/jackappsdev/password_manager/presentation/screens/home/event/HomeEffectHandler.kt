package com.jackappsdev.password_manager.presentation.screens.home.event

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class HomeEffectHandler(
    private val context: Context,
    private val navigator: Navigator,
    private val scope: CoroutineScope,
    private val filterBySheet: SheetState,
    private val sortBySheet: SheetState,
    private val lazyColumnState: LazyListState,
    private val keyboardController: SoftwareKeyboardController?,
    private val focusManager: FocusManager,
    private val onShowFilterBySheet: (Boolean) -> Unit,
    private val onShowSortBySheet: (Boolean) -> Unit
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
        onShowFilterBySheet(true)
    }

    fun onToggleSortSheetVisibility() {
        onShowSortBySheet(true)
    }

    fun onFilterSelected(categoryItems: List<CategoryModel>?) {
        scope.launch {
            if (categoryItems?.isNotEmpty() == true) {
                lazyColumnState.animateScrollToItem(0)
            }
            filterBySheet.hide()
        }.invokeOnCompletion {
            onShowFilterBySheet(false)
        }
    }

    fun onSortSelect(categoryItems: List<CategoryModel>?) {
        scope.launch {
            if (categoryItems?.isNotEmpty() == true) {
                lazyColumnState.animateScrollToItem(0)
            }
            sortBySheet.hide()
        }.invokeOnCompletion {
            onShowSortBySheet(false)
        }
    }

    fun onNavigateToPasswordItem(id: Int) {
        navigator.navigate(Routes.PasswordItemDetail(id))
    }

    fun onNavigateToAddPassword() {
        navigator.navigate(Routes.AddPasswordItem)
    }
}
