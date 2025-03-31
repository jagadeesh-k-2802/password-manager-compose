package com.jackappsdev.password_manager.shared.core

import android.content.Context
import android.widget.Toast

/**
 * Extension function to show a short toast
 * TODO: REPLACE ALL TOASTS WITH THIS FUNCTION
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
