package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailEffectHandler

@Composable
fun PasswordItemDetailRoot(navigator: Navigator, key: Routes.PasswordItemDetail) {
    val viewModel = hiltViewModel<PasswordItemDetailViewModel, PasswordItemDetailViewModel.Factory>(
        creationCallback = {
            factory -> factory.create(key)
        }
    )
    val context = LocalContext.current

    val effectHandler = remember {
        PasswordItemDetailEffectHandler(
            context = context,
            navigator = navigator,
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
