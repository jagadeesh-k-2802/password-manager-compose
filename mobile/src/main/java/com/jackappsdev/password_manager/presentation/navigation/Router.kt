package com.jackappsdev.password_manager.presentation.navigation

import android.os.Handler
import android.os.Looper
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jackappsdev.password_manager.constants.APP_AUTO_LOCK_DELAY
import com.jackappsdev.password_manager.presentation.components.BottomNavigationBar
import com.jackappsdev.password_manager.presentation.screens.add_category_item.AddCategoryItemScreen
import com.jackappsdev.password_manager.presentation.screens.add_password_item.AddPasswordItemScreen
import com.jackappsdev.password_manager.presentation.screens.android_watch.AndroidWatchScreen
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.CategoryItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordScreen
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordViewModel
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordEventHandler
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemScreen
import com.jackappsdev.password_manager.presentation.screens.home.HomeScreen
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesScreen
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesViewModel
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesEventHandler
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorScreen
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorViewModel
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorEventHandler
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockScreen
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockEventHandler
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsScreen
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsViewModel
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsEventHandler

/**
 * Manages all navigation routes
 */
@Composable
fun Router(
    navController: NavHostController,
    passwordLockViewModel: PasswordLockViewModel
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { contentPadding ->
        val state = passwordLockViewModel.state
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        val handler = remember { Handler(Looper.getMainLooper()) }
        val runnable = remember { Runnable { passwordLockViewModel.setUnlocked(false) } }

        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    handler.removeCallbacks(runnable)
                }

                if (event == Lifecycle.Event.ON_PAUSE) {
                    handler.postDelayed(runnable, APP_AUTO_LOCK_DELAY)
                }
            }

            lifecycle.addObserver(observer)
            onDispose { lifecycle.removeObserver(observer) }
        }

        if (!state.hasBeenUnlocked) {
            NavHost(
                navController,
                startDestination = Routes.PasswordLock,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.padding(contentPadding)
            ) {
                composable<Routes.PasswordLock> {
                    val activity = LocalActivity.current as FragmentActivity
                    val keyboardController = LocalSoftwareKeyboardController.current

                    val eventHandler = remember {
                        PasswordLockEventHandler(
                            activity = activity,
                            keyboardController = keyboardController,
                            onEvent = passwordLockViewModel::onEvent
                        )
                    }

                    PasswordLockScreen(
                        state = passwordLockViewModel.state,
                        effectFlow = passwordLockViewModel.effectFlow,
                        eventHandler = eventHandler,
                        errorFlow = passwordLockViewModel.errorFlow,
                        onEvent = passwordLockViewModel::onEvent
                    )
                }
            }
        } else {
            NavHost(
                navController,
                startDestination = Routes.Home,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.padding(contentPadding)
            ) {

                composable<Routes.Home> {
                    HomeScreen(navController)
                }

                composable<Routes.AddPasswordItem> {
                    AddPasswordItemScreen(navController)
                }

                composable<Routes.PasswordItemDetail> {
                    PasswordItemDetailScreen(navController)
                }

                composable<Routes.EditPasswordItem> {
                    EditPasswordItemScreen(navController)
                }

                composable<Routes.PasswordGenerator> {
                    val viewModel: PasswordGeneratorViewModel = hiltViewModel()
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val snackbarHostState = remember { SnackbarHostState() }

                    val eventHandler = remember {
                        PasswordGeneratorEventHandler(
                            context = context,
                            scope = scope,
                            snackbarHostState = snackbarHostState
                        )
                    }

                    PasswordGeneratorScreen(
                        snackbarHostState = snackbarHostState,
                        state = viewModel.state,
                        effectFlow = viewModel.effectFlow,
                        eventHandler = eventHandler,
                        onEvent = viewModel::onEvent
                    )
                }

                composable<Routes.Settings> {
                    val viewModel: SettingsViewModel = hiltViewModel()
                    val activity = LocalActivity.current as FragmentActivity

                    val eventHandler = remember {
                        SettingsEventHandler(
                            activity = activity,
                            onEvent = viewModel::onEvent
                        )
                    }

                    SettingsScreen(
                        navController = navController,
                        state = viewModel.state,
                        effectFlow = viewModel.effectFlow,
                        eventHandler = eventHandler,
                        onEvent = viewModel::onEvent,
                    )
                }

                composable<Routes.AndroidWatch> {
                    AndroidWatchScreen(navController)
                }

                composable<Routes.ChangePassword> {
                    val viewModel: ChangePasswordViewModel = hiltViewModel()
                    val context = LocalContext.current

                    val eventHandler = remember {
                        ChangePasswordEventHandler(
                            context = context,
                            navController = navController
                        )
                    }

                    ChangePasswordScreen(
                        navController = navController,
                        state = viewModel.state,
                        effectFlow = viewModel.effectFlow,
                        eventHandler = eventHandler,
                        errorFlow = viewModel.errorFlow,
                        onEvent = viewModel::onEvent,
                    )
                }

                composable<Routes.ManageCategories> {
                    val viewModel: ManageCategoriesViewModel = hiltViewModel()
                    val scope = rememberCoroutineScope()

                    val eventHandler = remember {
                        ManageCategoriesEventHandler(
                            scope = scope
                        )
                    }

                    ManageCategoriesScreen(
                        navController = navController,
                        state = viewModel.state,
                        effectFlow = viewModel.effectFlow,
                        eventHandler = eventHandler,
                        onEvent = viewModel::onEvent
                    )
                }

                composable<Routes.AddCategoryItem> {
                    AddCategoryItemScreen(navController)
                }

                composable<Routes.CategoryItemDetail> {
                    CategoryItemDetailScreen(navController)
                }
            }
        }
    }
}
