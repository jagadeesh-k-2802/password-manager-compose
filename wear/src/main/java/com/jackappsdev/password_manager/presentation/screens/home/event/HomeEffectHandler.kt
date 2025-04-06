package com.jackappsdev.password_manager.presentation.screens.home.event

import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.navigation.Routes

class HomeEffectHandler(
    private val navController: NavController
) {

    fun onNavigateToPasswordDetail(id: Int) {
        navController.navigate(Routes.PasswordItemDetail(id))
    }
}
