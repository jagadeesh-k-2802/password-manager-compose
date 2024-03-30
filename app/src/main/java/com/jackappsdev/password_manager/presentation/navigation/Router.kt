package com.jackappsdev.password_manager.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jackappsdev.password_manager.presentation.composables.BottomNavigationBar
import com.jackappsdev.password_manager.presentation.screens.add_category_item.AddCategoryItemScreen
import com.jackappsdev.password_manager.presentation.screens.add_password_item.AddPasswordItemScreen
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
        NavHost(
            navController,
            startDestination = Routes.PasswordLock.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Routes.PasswordLock.route) {
                PasswordLockScreen(navController, passwordLockViewModel)
            }

            composable(Routes.Home.route) {
                HomeScreen(navController)
            }

            composable(route = Routes.AddPasswordItem.route) {
                AddPasswordItemScreen(navController)
            }

            composable(Routes.PasswordItemDetail.route) {
                PasswordItemDetailScreen(navController)
            }

            composable(Routes.EditPasswordItem.route) {
                EditPasswordItemScreen(navController)
            }

            composable(Routes.PasswordGenerator.route) {
                PasswordGeneratorScreen()
            }

            composable(Routes.Settings.route) {
                SettingsScreen(navController)
            }

            composable(Routes.ChangePassword.route) {
                ChangePasswordScreen(navController)
            }

            composable(Routes.ManageCategories.route) {
                ManageCategoriesScreen(navController)
            }

            composable(Routes.AddCategoryItem.route) {
                AddCategoryItemScreen(navController)
            }

            composable(Routes.CategoryItemDetail.route) {
                CategoryItemDetailScreen(navController)
            }
        }
    }
}
