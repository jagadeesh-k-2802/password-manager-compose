package com.jagadeesh.passwordmanager.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jagadeesh.passwordmanager.presentation.composables.BottomNavigationBar
import com.jagadeesh.passwordmanager.presentation.screens.add_category_item.AddCategoryItemScreen
import com.jagadeesh.passwordmanager.presentation.screens.add_password_item.AddPasswordItemScreen
import com.jagadeesh.passwordmanager.presentation.screens.category_item_detail.CategoryItemDetailScreen
import com.jagadeesh.passwordmanager.presentation.screens.change_password.ChangePasswordScreen
import com.jagadeesh.passwordmanager.presentation.screens.edit_password_item.EditPasswordItemScreen
import com.jagadeesh.passwordmanager.presentation.screens.home.HomeScreen
import com.jagadeesh.passwordmanager.presentation.screens.manage_categories.ManageCategoriesScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_generator.PasswordGeneratorScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_item_detail.PasswordItemDetailScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_lock.PasswordLockScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_lock.PasswordLockViewModel
import com.jagadeesh.passwordmanager.presentation.screens.settings.SettingsScreen

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

            composable(
                route = Routes.AddPasswordItem.route,
                enterTransition = { slideInVertically(initialOffsetY = { it / 2 }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it / 2 }) },
            ) {
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

            composable(
                route = Routes.AddCategoryItem.route,
                enterTransition = { slideInVertically(initialOffsetY = { it / 2 }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it / 2 }) },
            ) {
                AddCategoryItemScreen(navController)
            }

            composable(Routes.CategoryItemDetail.route) {
                CategoryItemDetailScreen(navController)
            }
        }
    }
}
