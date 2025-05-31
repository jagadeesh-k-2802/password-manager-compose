package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.fragment.app.FragmentActivity
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockEffectHandler

@Composable
fun PasswordLockRoot(passwordLockViewModel: PasswordLockViewModel) {
    val activity = LocalActivity.current as FragmentActivity
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val effectHandler = remember {
        PasswordLockEffectHandler(
            activity = activity,
            keyboardController = keyboardController,
            focusRequester = focusRequester,
            onEvent = passwordLockViewModel::onEvent
        )
    }

    PasswordLockScreen(
        state = passwordLockViewModel.state,
        focusRequester = focusRequester,
        effectFlow = passwordLockViewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = passwordLockViewModel.errorFlow,
        onEvent = passwordLockViewModel::onEvent
    )
}
