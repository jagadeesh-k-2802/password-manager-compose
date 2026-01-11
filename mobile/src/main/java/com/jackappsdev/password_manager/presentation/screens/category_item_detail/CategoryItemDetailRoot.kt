package com.jackappsdev.password_manager.presentation.screens.category_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailEffectHandler

@Composable
fun CategoryItemDetailRoot(navigator: Navigator, key: Routes.CategoryItemDetail) {
    val viewModel = hiltViewModel<CategoryItemDetailViewModel, CategoryItemDetailViewModel.Factory>(
        creationCallback = {
                factory -> factory.create(key)
        }
    )

    val effectHandler = remember {
        CategoryItemDetailEffectHandler(
            navigator = navigator
        )
    }

    CategoryItemDetailScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
