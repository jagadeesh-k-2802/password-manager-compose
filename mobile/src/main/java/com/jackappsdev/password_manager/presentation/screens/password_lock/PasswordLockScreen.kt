package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_lock.components.EnterPasswordView
import com.jackappsdev.password_manager.presentation.screens.password_lock.components.SetupPasswordView
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockEffectHandler
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordLockScreen(
    state: PasswordLockState,
    effectFlow: Flow<PasswordLockUiEffect>,
    effectHandler: PasswordLockEffectHandler,
    errorFlow: Flow<PasswordLockError>,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    val error by errorFlow.collectAsState(null)

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is PasswordLockUiEffect.HideKeyboard -> onHideKeyboard()
                    is PasswordLockUiEffect.BiometricAuthenticate -> onBiometricAuthenticate()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (state.hasPasswordSet == true) {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.title_enter_password)) },
                    windowInsets = windowInsetsVerticalZero
                )
            } else {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.title_create_password)) },
                    windowInsets = windowInsetsVerticalZero
                )
            }
        }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
        ) {
            when (state.hasPasswordSet) {
                false, null -> SetupPasswordView(state, error, onEvent)
                true -> EnterPasswordView(state, error, onEvent)
            }
        }
    }
}
