package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailEffectHandler

@Composable
fun PasswordItemDetailRoot(navController: NavController) {
    val viewModel: PasswordItemDetailViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember {
        PasswordItemDetailEffectHandler(
            context = context,
            navController = navController,
            onEvent = viewModel::onEvent
        )
    }

    PasswordItemDetailScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
