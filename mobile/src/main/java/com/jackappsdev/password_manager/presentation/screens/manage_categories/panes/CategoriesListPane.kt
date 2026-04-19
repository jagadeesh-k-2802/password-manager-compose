package com.jackappsdev.password_manager.presentation.screens.manage_categories.panes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesScreen
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesViewModel
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesEffectHandler

@Composable
internal fun CategoriesListPane(
    onNavigateToCategoryItem: (Int) -> Unit,
    onNavigateToAddCategory: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val viewModel: ManageCategoriesViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    val effectHandler = remember {
        ManageCategoriesEffectHandler(
            scope = scope,
            navigateToCategoryItem = onNavigateToCategoryItem,
            navigateToAddCategory = onNavigateToAddCategory,
            navigateUp = onNavigateUp
        )
    }

    ManageCategoriesScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
