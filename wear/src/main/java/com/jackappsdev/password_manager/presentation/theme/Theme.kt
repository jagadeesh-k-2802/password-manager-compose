package com.jackappsdev.password_manager.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.MotionScheme
import androidx.wear.compose.material3.dynamicColorScheme

@Composable
fun PasswordManagerTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val dynamicColorScheme = dynamicColorScheme(context)

    MaterialTheme(
        colorScheme = dynamicColorScheme ?: passwordManagerColorScheme,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}
