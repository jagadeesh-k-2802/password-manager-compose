package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController

class AddPasswordItemEffectHandler(
    val navController: NavController,
    val keyboardController: SoftwareKeyboardController?
) {

    fun onNavigateUp() {
        keyboardController?.hide()
        navController.navigateUp()
    }
}
