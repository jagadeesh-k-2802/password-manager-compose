package com.jackappsdev.password_manager.presentation.screens.password_generator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_generator.components.GeneratedPasswordView
import com.jackappsdev.password_manager.presentation.screens.password_generator.components.LengthSliderView
import com.jackappsdev.password_manager.presentation.screens.password_generator.components.OptionsView
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorEffectHandler
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(
    snackbarHostState: SnackbarHostState,
    state: PasswordGeneratorState,
    effectFlow: Flow<PasswordGeneratorUiEffect>,
    effectHandler: PasswordGeneratorEffectHandler,
    onEvent: (PasswordGeneratorUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is PasswordGeneratorUiEffect.OnCopyToClipboard -> onCopyText(effect.text)
                    is PasswordGeneratorUiEffect.OnShowSnackbarMessage -> onShowSnackbarMessage(effect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_password_generator)) },
                windowInsets = windowInsetsVerticalZero
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            GeneratedPasswordView(state, onEvent)
            LengthSliderView(state, onEvent)
            Spacer(modifier = Modifier.height(pagePadding))
            OptionsView(state, onEvent)
        }
    }
}
