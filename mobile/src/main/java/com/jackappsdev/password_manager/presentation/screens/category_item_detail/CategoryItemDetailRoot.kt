package com.jackappsdev.password_manager.presentation.screens.category_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailEffectHandler

@Composable
fun CategoryItemDetailRoot(navController: NavController) {
    val viewModel: CategoryItemDetailViewModel = hiltViewModel()

    val effectHandler = remember {
        CategoryItemDetailEffectHandler(
            navController = navController
        )
    }

    CategoryItemDetailScreen(
        navController = navController,
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
