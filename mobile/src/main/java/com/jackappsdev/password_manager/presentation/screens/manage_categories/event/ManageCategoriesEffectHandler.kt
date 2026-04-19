package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ManageCategoriesEffectHandler(
    private val scope: CoroutineScope,
    private val navigateToCategoryItem: (Int) -> Unit,
    private val navigateToAddCategory: () -> Unit,
    private val navigateUp: () -> Unit
) {
    fun onScrollToTop(state: LazyListState) {
        scope.launch { state.animateScrollToItem(0) }
    }

    fun onNavigateToCategoryItem(id: Int) {
        navigateToCategoryItem(id)
    }

    fun onNavigateToAddCategory() {
        navigateToAddCategory()
    }

    fun onNavigateUp() {
        navigateUp()
    }
}
