package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailEffectHandler

@Composable
fun PasswordItemDetailRoot(navController: NavController) {
    val viewModel: PasswordItemDetailViewModel = hiltViewModel()

    val effectHandler = remember {
        PasswordItemDetailEffectHandler(
            navController = navController
        )
    }

    PasswordItemDetailScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
