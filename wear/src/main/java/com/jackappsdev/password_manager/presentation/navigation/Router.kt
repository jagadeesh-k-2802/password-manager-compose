package com.jackappsdev.password_manager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.jackappsdev.password_manager.presentation.screens.home.HomeScreen
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockScreen

@Composable
fun Router(navController: NavHostController) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Routes.PasswordLock.route
    ) {
        composable(Routes.PasswordLock.route) { PasswordLockScreen()  }
        composable(Routes.Home.route) { HomeScreen() }
        composable(Routes.PasswordItemDetail.route) { PasswordItemDetailScreen() }
    }
}
