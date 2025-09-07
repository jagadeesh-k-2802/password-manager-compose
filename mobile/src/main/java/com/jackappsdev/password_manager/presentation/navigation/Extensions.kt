package com.jackappsdev.password_manager.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute

fun NavController.navigateWithState(route: Routes) {
    val alreadyVisible = when (route) {
        Routes.Home -> currentDestination?.hasRoute<Routes.Home>()
        Routes.PasswordGenerator -> currentDestination?.hasRoute<Routes.PasswordGenerator>()
        Routes.Settings -> currentDestination?.hasRoute<Routes.Settings>()
        else -> false
    } ?: false

    if (alreadyVisible) {
        return
    }

    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

fun <T : Any> NavController.replace(route: T) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
