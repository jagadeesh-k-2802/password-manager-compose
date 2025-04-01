package com.jackappsdev.password_manager.presentation.screens.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsEffectHandler

@Composable
fun SettingsRoot(navController: NavController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val activity = LocalActivity.current as FragmentActivity

    val effectHandler = remember {
        SettingsEffectHandler(
            activity = activity,
            onEvent = viewModel::onEvent
        )
    }

    SettingsScreen(
        navController = navController,
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
