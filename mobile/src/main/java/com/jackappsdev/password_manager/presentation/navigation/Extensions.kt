package com.jackappsdev.password_manager.presentation.navigation

import androidx.navigation.NavController

fun NavController.navigateWithState(route: Routes) {
    navigate(route) {
        popUpTo(Graph.UnlockedGraph) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
