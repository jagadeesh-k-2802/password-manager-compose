package com.jackappsdev.password_manager.presentation.screens.change_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordEffectHandler

@Composable
fun ChangePasswordRoot(navigator: Navigator) {
    val viewModel: ChangePasswordViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember {
        ChangePasswordEffectHandler(
            context = context,
            navigator = navigator
        )
    }

    ChangePasswordScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = viewModel.errorFlow,
        onEvent = viewModel::onEvent,
    )
}
