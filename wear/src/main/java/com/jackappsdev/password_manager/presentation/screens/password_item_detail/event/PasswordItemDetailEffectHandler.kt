package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

import android.content.Context
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.core.showToast

class PasswordItemDetailEffectHandler(
    private val context: Context,
    private val navController: NavController
) {

    fun onNavigateUp() {
        navController.navigateUp()
    }

    fun onPasswordRemoved() {
        context.showToast(context.getString(R.string.toast_password_removed))
        navController.navigateUp()
    }
}
