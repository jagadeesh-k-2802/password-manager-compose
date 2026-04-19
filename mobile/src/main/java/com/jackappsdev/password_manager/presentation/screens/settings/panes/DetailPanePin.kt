package com.jackappsdev.password_manager.presentation.screens.settings.panes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.screens.pin.PinScreen
import com.jackappsdev.password_manager.presentation.screens.pin.PinViewModel
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinEffectHandler

@Composable
internal fun DetailPanePin(showBackButton: Boolean, onNavigateUp: () -> Unit) {
    val viewModel: PinViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember(onNavigateUp) {
        PinEffectHandler(
            context = context,
            navigateUp = onNavigateUp
        )
    }

    PinScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = viewModel.errorFlow,
        onEvent = viewModel::onEvent,
        showBackButton = showBackButton
    )
}
