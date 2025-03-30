package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.jackappsdev.password_manager.presentation.screens.password_lock.components.NoPhoneAppView
import com.jackappsdev.password_manager.presentation.screens.password_lock.components.EnterPinView
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockEventHandler
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PasswordLockScreen(
    state: PasswordLockState,
    eventHandler: PasswordLockEventHandler,
    effectFlow: Flow<PasswordLockUiEffect>,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with (eventHandler) {
                when (effect) {
                    is PasswordLockUiEffect.Unlock -> onUnlock()
                    is PasswordLockUiEffect.IncorrectPassword -> onIncorrectPassword()
                    is PasswordLockUiEffect.OpenPhoneApp -> onOpenPhoneApp()
                }
            }
        }
    }

    when (state.hasPinSet) {
        false, null -> NoPhoneAppView(onEvent)
        true -> EnterPinView(state, onEvent)
    }
}
