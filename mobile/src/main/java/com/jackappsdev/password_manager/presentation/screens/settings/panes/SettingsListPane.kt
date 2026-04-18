package com.jackappsdev.password_manager.presentation.screens.settings.panes

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsScreen
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsViewModel
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsEffectHandler

@Composable
internal fun SettingsListPane(
    navigator: Navigator,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToPin: () -> Unit,
    onNavigateToAndroidWatch: () -> Unit
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val activity = LocalActivity.current as FragmentActivity
    val scope = rememberCoroutineScope()

    val effectHandler = remember {
        SettingsEffectHandler(
            activity = activity,
            scope = scope,
            onEvent = viewModel::onEvent,
            navigateToChangePassword = onNavigateToChangePassword,
            navigateToManageCategories = { navigator.navigate(Routes.ManageCategories) },
            navigateToAndroidWatch = onNavigateToAndroidWatch,
            navigateToPin = onNavigateToPin
        )
    }

    SettingsScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
