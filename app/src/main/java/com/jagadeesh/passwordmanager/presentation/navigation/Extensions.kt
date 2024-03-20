package com.jagadeesh.passwordmanager.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigate(route: Routes) {
    navigate(route.route)
}

fun NavController.navigateWithState(route: Routes) {
    navigate(route.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.replace(route: Routes) {
    navigate(route.route) {
        popUpTo(graph.findStartDestination().id) { inclusive = true }
    }
}
