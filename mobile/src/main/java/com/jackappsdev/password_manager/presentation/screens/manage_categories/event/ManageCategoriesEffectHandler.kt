package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

import androidx.compose.foundation.lazy.LazyListState
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ManageCategoriesEffectHandler(
    private val navigator: Navigator,
    private val scope: CoroutineScope
) {
    fun onScrollToTop(state: LazyListState) {
        scope.launch { state.animateScrollToItem(0) }
    }

    fun onNavigateToCategoryItem(id: Int) {
        navigator.navigate(Routes.CategoryItemDetail(id))
    }

    fun onNavigateToAddCategory() {
        navigator.navigate(Routes.AddCategoryItem)
    }

    fun onNavigateUp() {
        navigator.navigateUp()
    }
}
