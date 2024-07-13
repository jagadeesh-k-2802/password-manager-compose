package com.jackappsdev.password_manager.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Contains helper utilities to build responsive user interfaces
 */

const val LARGE_DISPLAY_BREAKPOINT = 225

@Composable
fun isLargeDisplay() = LocalConfiguration.current.screenWidthDp >= LARGE_DISPLAY_BREAKPOINT
