package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

import androidx.compose.foundation.lazy.LazyListState
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ManageCategoriesEffectHandler(
    private val navController: NavController,
    private val scope: CoroutineScope
) {
    fun onScrollToTop(state: LazyListState) {
        scope.launch {
            state.animateScrollToItem(0)
        }
    }

    fun onNavigateToAddCategory() {
        navController.navigate(Routes.AddCategoryItem)
    }

    fun onNavigateToCategoryItem(id: Int) {
        navController.navigate(Routes.CategoryItemDetail(id))
    }

    fun onNavigateUp() {
        navController.navigateUp()
    }
}
