package com.jackappsdev.password_manager.presentation.screens.manage_categories.panes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.CategoryItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.CategoryItemDetailViewModel
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailEffectHandler

@Composable
internal fun DetailPaneCategoryDetail(
    id: Int,
    showBackButton: Boolean,
    onNavigateUp: () -> Unit
) {
    val key = Routes.CategoryItemDetail(id)
    val viewModel = hiltViewModel<CategoryItemDetailViewModel, CategoryItemDetailViewModel.Factory>(
        key = "category_detail_$id",
        creationCallback = { factory -> factory.create(key) }
    )

    val effectHandler = remember(id, onNavigateUp) {
        CategoryItemDetailEffectHandler(
            navigateUp = onNavigateUp
        )
    }

    CategoryItemDetailScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
        showBackButton = showBackButton
    )
}
