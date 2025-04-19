package com.jackappsdev.password_manager.core

import androidx.compose.ui.Modifier

/**
 * Extension function to conditionally apply a [modifier] based on [condition].
 */
fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
