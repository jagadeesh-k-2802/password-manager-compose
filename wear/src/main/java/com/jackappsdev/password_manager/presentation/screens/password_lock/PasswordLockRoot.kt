package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockEffectHandler

@Composable
fun PasswordLockRoot(passwordLockViewModel: PasswordLockViewModel) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val effectHandler = remember { PasswordLockEffectHandler(context, scope, haptic) }

    PasswordLockScreen(
        state = passwordLockViewModel.state,
        effectHandler = effectHandler,
        effectFlow = passwordLockViewModel.effectFlow,
        onEvent = passwordLockViewModel::onEvent
    )
}
