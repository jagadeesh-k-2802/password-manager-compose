package com.jackappsdev.password_manager.presentation.screens.pin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinEffectHandler

@Composable
fun PinRoot(navigator: Navigator) {
    val viewModel: PinViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember {
        PinEffectHandler(
            context = context,
            navigator = navigator
        )
    }

    PinScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
        errorFlow = viewModel.errorFlow
    )
}
