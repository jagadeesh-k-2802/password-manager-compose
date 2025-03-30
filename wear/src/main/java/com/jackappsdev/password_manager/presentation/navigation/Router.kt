package com.jackappsdev.password_manager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.google.android.horologist.compose.layout.AppScaffold
import com.jackappsdev.password_manager.presentation.screens.home.HomeScreen
import com.jackappsdev.password_manager.presentation.screens.home.HomeViewModel
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailViewModel
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockScreen
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockEventHandler

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
                composable(Routes.PasswordLock.route) {
                    val viewModel: PasswordLockViewModel = hiltViewModel()
                    val context = LocalContext.current
                    val haptic = LocalHapticFeedback.current
                    val scope = rememberCoroutineScope()
                    val eventHandler = PasswordLockEventHandler(context, scope, haptic)

                    PasswordLockScreen(
                        state = viewModel.state,
                        eventHandler = eventHandler,
                        effectFlow = viewModel.effectFlow,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        } else {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = Routes.Home.route
            ) {
                composable(Routes.Home.route) {
                    val viewModel: HomeViewModel = hiltViewModel()

                    HomeScreen(
                        navigateToDetail = { navController.navigate(Routes.PasswordItemDetail(it)) },
                        viewModel = viewModel
                    )
                }

                composable(Routes.PasswordItemDetail.route) {
                    val viewModel: PasswordItemDetailViewModel = hiltViewModel()

                    PasswordItemDetailScreen(
                        viewModel = viewModel,
                        popBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}
