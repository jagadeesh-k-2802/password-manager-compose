package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.navigation.Routes

class AddPasswordItemEffectHandler(
    val navController: NavController,
    val keyboardController: SoftwareKeyboardController?
) {

    fun onNavigateToAddCategory() {
        navController.navigate(Routes.AddCategoryItem)
    }

    fun onNavigateUp() {
        keyboardController?.hide()
        navController.navigateUp()
    }
}
