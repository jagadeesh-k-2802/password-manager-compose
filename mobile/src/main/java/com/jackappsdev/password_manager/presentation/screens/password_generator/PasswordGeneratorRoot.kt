package com.jackappsdev.password_manager.presentation.screens.password_generator

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorEffectHandler

@Composable
fun PasswordGeneratorRoot() {
    val viewModel: PasswordGeneratorViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val effectHandler = remember {
        PasswordGeneratorEffectHandler(
            context = context,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }

    PasswordGeneratorScreen(
        snackbarHostState = snackbarHostState,
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
