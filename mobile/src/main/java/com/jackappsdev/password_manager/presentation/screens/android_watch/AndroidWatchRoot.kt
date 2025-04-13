package com.jackappsdev.password_manager.presentation.screens.android_watch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchEffectHandler

@Composable
fun AndroidWatchRoot(navController: NavController) {
    val viewModel: AndroidWatchViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val effectHandler = remember {
        AndroidWatchEffectHandler(
            context = context,
            navController = navController,
            scope = scope,
            keyboardController = keyboardController,
            onEvent = viewModel::onEvent
        )
    }

    AndroidWatchScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = viewModel.errorFlow,
        onEvent = viewModel::onEvent,
    )
}
