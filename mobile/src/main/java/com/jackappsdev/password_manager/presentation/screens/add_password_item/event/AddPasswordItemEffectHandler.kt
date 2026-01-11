package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import androidx.compose.ui.platform.SoftwareKeyboardController
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes

class AddPasswordItemEffectHandler(
    val navigator: Navigator,
    val keyboardController: SoftwareKeyboardController?
) {

    fun onNavigateToAddCategory() {
        navigator.navigate(Routes.AddCategoryItem)
    }

    fun onNavigateUp() {
        keyboardController?.hide()
        navigator.navigateUp()
    }
}
