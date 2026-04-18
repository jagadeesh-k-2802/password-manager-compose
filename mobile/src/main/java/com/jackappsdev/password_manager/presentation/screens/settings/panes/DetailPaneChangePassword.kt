package com.jackappsdev.password_manager.presentation.screens.settings.panes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordScreen
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordViewModel
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordEffectHandler

@Composable
internal fun DetailPaneChangePassword(showBackButton: Boolean, onNavigateUp: () -> Unit) {
    val viewModel: ChangePasswordViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember(onNavigateUp) {
        ChangePasswordEffectHandler(
            context = context,
            navigateUp = onNavigateUp
        )
    }

    ChangePasswordScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = viewModel.errorFlow,
        onEvent = viewModel::onEvent,
        showBackButton = showBackButton
    )
}
