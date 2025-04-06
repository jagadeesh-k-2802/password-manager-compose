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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jackappsdev.password_manager.constants.APP_AUTO_LOCK_DELAY
import com.jackappsdev.password_manager.presentation.components.BottomNavigationBar
import com.jackappsdev.password_manager.presentation.screens.add_category_item.AddCategoryItemRoot
import com.jackappsdev.password_manager.presentation.screens.add_password_item.AddPasswordItemRoot
import com.jackappsdev.password_manager.presentation.screens.android_watch.AndroidWatchRoot
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.CategoryItemDetailRoot
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordRoot
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemRoot
import com.jackappsdev.password_manager.presentation.screens.home.HomeRoot
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesRoot
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorRoot
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsRoot

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
                composable<Routes.PasswordLock> { PasswordLockRoot(passwordLockViewModel) }
            }
        } else {
            NavHost(
                navController,
                startDestination = Routes.Home,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.padding(contentPadding)
            ) {
                composable<Routes.Home> { HomeRoot(navController) }
                composable<Routes.AddPasswordItem> { AddPasswordItemRoot(navController) }
                composable<Routes.PasswordItemDetail> { PasswordItemDetailRoot(navController) }
                composable<Routes.EditPasswordItem> { EditPasswordItemRoot(navController) }
                composable<Routes.PasswordGenerator> { PasswordGeneratorRoot() }
                composable<Routes.Settings> { SettingsRoot(navController) }
                composable<Routes.AndroidWatch> { AndroidWatchRoot(navController) }
                composable<Routes.ChangePassword> { ChangePasswordRoot(navController) }
                composable<Routes.ManageCategories> { ManageCategoriesRoot(navController) }
                composable<Routes.AddCategoryItem> { AddCategoryItemRoot(navController) }
                composable<Routes.CategoryItemDetail> { CategoryItemDetailRoot(navController) }
            }
        }
    }
}
