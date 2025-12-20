package com.jackappsdev.password_manager.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme

val Primary = Color(0xFF2196F3)
val SurfaceContainer = Color(0xFF303133)
val PrimaryDim = Color(0xFF212E68)
val PrimaryContainer = Color(0xFF1E3E57)
val Secondary = Color(0xFFB3E5FC)

internal val passwordManagerColorScheme = ColorScheme(
    primary = Primary,
    primaryDim = PrimaryDim,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    secondaryDim = Secondary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onError = Color.Black,
    surfaceContainer = SurfaceContainer
)
