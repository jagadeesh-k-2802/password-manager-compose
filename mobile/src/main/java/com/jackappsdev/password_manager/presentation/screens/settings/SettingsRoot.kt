package com.jackappsdev.password_manager.presentation.screens.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsEffectHandler

@Composable
fun SettingsRoot(navController: NavController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val activity = LocalActivity.current as FragmentActivity
    val scope = rememberCoroutineScope()

    val effectHandler = remember {
        SettingsEffectHandler(
            activity = activity,
            navController = navController,
            scope = scope,
            onEvent = viewModel::onEvent
        )
    }

    SettingsScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
