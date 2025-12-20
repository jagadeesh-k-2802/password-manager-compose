package com.jackappsdev.password_manager.presentation.screens.change_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.change_password.components.ChangePasswordView
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordEffectHandler
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEffect
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    state: ChangePasswordState,
    effectFlow: Flow<ChangePasswordUiEffect>,
    effectHandler: ChangePasswordEffectHandler,
    errorFlow: Flow<ChangePasswordError>,
    onEvent: (ChangePasswordUiEvent) -> Unit
) {
    val error by errorFlow.collectAsState(initial = null)
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is ChangePasswordUiEffect.PasswordUpdated -> onPasswordUpdated()
                    is ChangePasswordUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onEvent(ChangePasswordUiEvent.NavigateUp) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_change_password)) }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            ChangePasswordView(
                state = state,
                error = error,
                onEvent = onEvent
            )
        }
    }
}
