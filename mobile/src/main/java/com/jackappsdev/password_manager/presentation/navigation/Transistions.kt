package com.jackappsdev.password_manager.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

/**
 * A vertical slide transition for navigation between screens.
 * New content slides in from the bottom, while old content slides out to the bottom.
 */
internal val verticalTransition = NavDisplay.transitionSpec {
    // Slide new content up, keeping the old content in place underneath
    slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(400)
    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
} + NavDisplay.popTransitionSpec {
    // Slide old content down, revealing the new content in place underneath
    EnterTransition.None togetherWith slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(400)
    )
} + NavDisplay.predictivePopTransitionSpec {
    // Slide old content down, revealing the new content in place underneath
    EnterTransition.None togetherWith slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(400)
    )
}
