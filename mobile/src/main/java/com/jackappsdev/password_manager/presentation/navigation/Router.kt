package com.jackappsdev.password_manager.presentation.navigation

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jackappsdev.password_manager.presentation.composables.BottomNavigationBar
import com.jackappsdev.password_manager.presentation.screens.add_category_item.AddCategoryItemScreen
import com.jackappsdev.password_manager.presentation.screens.add_password_item.AddPasswordItemScreen
import com.jackappsdev.password_manager.presentation.screens.android_watch.AndroidWatchScreen
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.CategoryItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordScreen
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemScreen
import com.jackappsdev.password_manager.presentation.screens.home.HomeScreen
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesScreen
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorScreen
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockScreen
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsScreen

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
                    handler.postDelayed(runnable, 30000)
                }
            }

            lifecycle.addObserver(observer)

            onDispose {
                lifecycle.removeObserver(observer)
            }
        }

        if (!state.hasBeenUnlocked) {
            NavHost(
                navController,
                startDestination = Routes.PasswordLock.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(Routes.PasswordLock.route) { PasswordLockScreen(passwordLockViewModel) }
            }
        } else {
            NavHost(
                navController,
                startDestination = Routes.Home.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(Routes.Home.route) { HomeScreen(navController) }
                composable(Routes.AddPasswordItem.route) { AddPasswordItemScreen(navController) }
                composable(Routes.PasswordItemDetail.route) { PasswordItemDetailScreen(navController) }
                composable(Routes.EditPasswordItem.route) { EditPasswordItemScreen(navController) }
                composable(Routes.PasswordGenerator.route) { PasswordGeneratorScreen() }
                composable(Routes.Settings.route) { SettingsScreen(navController) }
                composable(Routes.AndroidWatch.route) { AndroidWatchScreen(navController) }
                composable(Routes.ChangePassword.route) { ChangePasswordScreen(navController) }
                composable(Routes.ManageCategories.route) { ManageCategoriesScreen(navController) }
                composable(Routes.AddCategoryItem.route) { AddCategoryItemScreen(navController) }
                composable(Routes.CategoryItemDetail.route) { CategoryItemDetailScreen(navController) }
            }
        }
    }
}
