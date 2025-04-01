package com.jackappsdev.password_manager.presentation.screens.category_item_detail.event

import androidx.navigation.NavController

class CategoryItemDetailEffectHandler(
    private val navController: NavController
) {

    fun onNavigateUp() {
        navController.navigateUp()
    }
}
