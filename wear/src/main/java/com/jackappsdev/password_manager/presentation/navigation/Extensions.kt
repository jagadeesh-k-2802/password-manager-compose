package com.jackappsdev.password_manager.presentation.navigation

import androidx.navigation.NavController

fun NavController.replace(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}
