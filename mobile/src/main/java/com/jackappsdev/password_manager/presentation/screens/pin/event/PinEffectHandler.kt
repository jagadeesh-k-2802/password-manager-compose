package com.jackappsdev.password_manager.presentation.screens.pin.event

import android.content.Context
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.core.showToast

class PinEffectHandler(
    private val context: Context,
    private val navController: NavController
) {

    fun onPinUpdated() {
        context.showToast(context.getString(R.string.toast_pin_set))
    }

    fun onNavigateUp() {
        navController.navigateUp()
    }
}
