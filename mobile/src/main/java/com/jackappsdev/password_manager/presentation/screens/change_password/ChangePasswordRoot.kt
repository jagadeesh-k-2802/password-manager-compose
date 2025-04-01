package com.jackappsdev.password_manager.presentation.screens.change_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordEffectHandler

@Composable
fun ChangePasswordRoot(navController: NavController) {
    val viewModel: ChangePasswordViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember {
        ChangePasswordEffectHandler(
            context = context,
            navController = navController
        )
    }

    ChangePasswordScreen(
        navController = navController,
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = viewModel.errorFlow,
        onEvent = viewModel::onEvent,
    )
}
