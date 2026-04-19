package com.jackappsdev.password_manager.presentation.screens.settings.panes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.screens.android_watch.AndroidWatchScreen
import com.jackappsdev.password_manager.presentation.screens.android_watch.AndroidWatchViewModel
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchEffectHandler

@Composable
internal fun DetailPaneAndroidWatch(showBackButton: Boolean, onNavigateUp: () -> Unit) {
    val viewModel: AndroidWatchViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val effectHandler = remember(onNavigateUp) {
        AndroidWatchEffectHandler(
            context = context,
            navigateUp = onNavigateUp,
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
        showBackButton = showBackButton
    )
}
