package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

import androidx.navigation.NavController

class PasswordItemDetailEffectHandler(
    private val navController: NavController
) {

    fun onNavigateUp() {
        navController.navigateUp()
    }
}
