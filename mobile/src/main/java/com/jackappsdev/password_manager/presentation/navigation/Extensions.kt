package com.jackappsdev.password_manager.presentation.navigation

import androidx.navigation.NavController

fun NavController.navigateWithState(route: Routes) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

fun <T : Any> NavController.replace(route: T) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}
