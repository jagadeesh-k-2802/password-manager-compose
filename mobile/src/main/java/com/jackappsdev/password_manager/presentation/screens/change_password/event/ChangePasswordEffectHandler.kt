package com.jackappsdev.password_manager.presentation.screens.change_password.event

import android.content.Context
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.core.showToast

class ChangePasswordEffectHandler(
    private val context: Context,
    private val navController: NavController
) {

    fun onPasswordChanged() {
        navController.navigateUp()
        context.showToast(context.getString(R.string.toast_password_changed_successfully))
    }
}
