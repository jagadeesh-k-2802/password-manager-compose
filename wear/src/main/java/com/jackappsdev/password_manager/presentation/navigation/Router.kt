package com.jackappsdev.password_manager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.google.android.horologist.compose.layout.AppScaffold
import com.jackappsdev.password_manager.presentation.screens.home.HomeRoot
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel

/**
 * Manages all navigation routes
 */
@Composable
fun Router(
    navController: NavHostController,
    passwordLockViewModel: PasswordLockViewModel
) {
    val state = passwordLockViewModel.state
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                passwordLockViewModel.lockApp()
            }
        }

        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    AppScaffold {
        if (!state.hasBeenUnlocked) {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = Routes.PasswordLock.route
            ) {
                composable(Routes.PasswordLock.route) { PasswordLockRoot(passwordLockViewModel) }
            }
        } else {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = Routes.Home.route
            ) {
                composable(Routes.Home.route) { HomeRoot(navController) }
                composable(Routes.PasswordItemDetail.route) { PasswordItemDetailRoot(navController) }
            }
        }
    }
}
