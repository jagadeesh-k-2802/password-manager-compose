package com.jackappsdev.password_manager.presentation.screens.manage_categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesEffectHandler

@Composable
fun ManageCategoriesRoot(navController: NavController) {
    val viewModel: ManageCategoriesViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    val effectHandler = remember {
        ManageCategoriesEffectHandler(
            navController = navController,
            scope = scope
        )
    }

    ManageCategoriesScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
