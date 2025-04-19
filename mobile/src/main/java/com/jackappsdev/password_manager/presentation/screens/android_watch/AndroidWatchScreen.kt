package com.jackappsdev.password_manager.presentation.screens.android_watch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.components.ToggleSettingItem
import com.jackappsdev.password_manager.presentation.screens.android_watch.components.DisableAndroidWatchDialog
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchEffectHandler
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchUiEffect
import com.jackappsdev.password_manager.presentation.screens.android_watch.event.AndroidWatchUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndroidWatchScreen(
    state: AndroidWatchState,
    effectFlow: Flow<AndroidWatchUiEffect>,
    effectHandler: AndroidWatchEffectHandler,
    errorFlow: Flow<AndroidWatchError>,
    onEvent: (AndroidWatchUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val error by errorFlow.collectAsState(initial = null)

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is AndroidWatchUiEffect.RequestPinChange -> onRequestPinChange()
                    is AndroidWatchUiEffect.SetupPin -> onSetupPin(effect.pin)
                    is AndroidWatchUiEffect.ConfirmToggleAndroidWatch -> onConfirmToggleAndroidWatch()
                    is AndroidWatchUiEffect.DisableAndroidWatchSharing -> onDisableWatchSharing()
                    is AndroidWatchUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    if (state.showDisableAndroidWatchDialog) {
        DisableAndroidWatchDialog(
            onConfirm = { onEvent(AndroidWatchUiEvent.DisableAndroidWatchSharing) },
            onDismiss = { onEvent(AndroidWatchUiEvent.ToggleDisableAndroidWatchDialogVisibility) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onEvent(AndroidWatchUiEvent.NavigateUp) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_android_watch)) },
                windowInsets = windowInsetsVerticalZero
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            ToggleSettingItem(
                title = stringResource(R.string.label_enable_android_watch),
                subtitle = stringResource(R.string.text_android_watch_note),
                checked = state.useAndroidWatch == true,
                onClick = { onEvent(AndroidWatchUiEvent.RequestToggleAndroidWatch) }
            )

            if (state.useAndroidWatch == true) {
                Column {
                    OutlinedTextField(
                        value = state.pin,
                        onValueChange = { onEvent(AndroidWatchUiEvent.EnterPin(it)) },
                        label = {
                            Text(
                                stringResource(
                                    if (state.hasAndroidWatchPinSet != true) {
                                        R.string.label_enter_pin
                                    }
                                    else {
                                        R.string.label_update_pin
                                    }
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = pagePadding)
                            .fillMaxWidth(),
                        isError = error is AndroidWatchError.PinError,
                        singleLine = true,
                        supportingText = {
                            error?.let {
                                if (it is AndroidWatchError.PinError) Text(stringResource(it.error))
                            }
                        },
                        visualTransformation = if (state.showPin) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(AndroidWatchUiEvent.ToggleShowPinVisibility) }) {
                                Icon(
                                    imageVector = if (state.showPin) {
                                        Icons.Outlined.VisibilityOff
                                    } else {
                                        Icons.Outlined.Visibility
                                    },
                                    contentDescription = stringResource(R.string.accessibility_toggle_password)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onEvent(AndroidWatchUiEvent.RequestPinChange) }
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { onEvent(AndroidWatchUiEvent.RequestPinChange) },
                        modifier = Modifier
                            .padding(horizontal = pagePadding)
                            .fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Outlined.Done, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            stringResource(
                                if (state.hasAndroidWatchPinSet != true) {
                                    R.string.btn_set_pin
                                } else {
                                    R.string.btn_update_pin
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}
