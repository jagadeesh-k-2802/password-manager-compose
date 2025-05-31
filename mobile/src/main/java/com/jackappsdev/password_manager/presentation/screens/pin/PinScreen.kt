package com.jackappsdev.password_manager.presentation.screens.pin

import androidx.compose.animation.AnimatedVisibility
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
import com.jackappsdev.password_manager.presentation.components.ConfirmationDialog
import com.jackappsdev.password_manager.presentation.components.ToggleSettingItem
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinEffectHandler
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinUiEffect
import com.jackappsdev.password_manager.presentation.screens.pin.event.PinUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinScreen(
    state: PinState,
    effectFlow: Flow<PinUiEffect>,
    effectHandler: PinEffectHandler,
    errorFlow: Flow<PinError>,
    onEvent: (PinUiEvent) -> Unit,
) {
    val scrollState = rememberScrollState()
    val error by errorFlow.collectAsState(initial = null)

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is PinUiEffect.PinUpdated -> onPinUpdated()
                    is PinUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    if (state.showDisablePinDialog) {
        ConfirmationDialog(
            title = R.string.dialog_title_disable_pin_usage,
            description = R.string.dialog_text_disable_pin_usage,
            onConfirm = { onEvent(PinUiEvent.DisablePin) },
            onDismiss = { onEvent(PinUiEvent.ToggleDisablePinDialogVisibility) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onEvent(PinUiEvent.NavigateUp) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_pin)) },
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
                title = stringResource(R.string.label_enable_pin),
                subtitle = stringResource(R.string.text_pin_note),
                checked = state.usePin == true,
                onClick = { onEvent(PinUiEvent.TogglePin) }
            )

            AnimatedVisibility(state.usePin == true) {
                Column {
                    OutlinedTextField(
                        value = state.pin,
                        onValueChange = { onEvent(PinUiEvent.EnterPin(it)) },
                        label = {
                            Text(
                                stringResource(
                                    if (state.hasPinSet != true) {
                                        R.string.label_set_pin
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
                        isError = error is PinError,
                        singleLine = true,
                        supportingText = {
                            error?.let {
                                if (it is PinError.PinInputError) Text(stringResource(it.error))
                            }
                        },
                        visualTransformation = if (state.showPin) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(PinUiEvent.ToggleShowPinVisibility) }) {
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
                            onDone = { onEvent(PinUiEvent.ChangePin) }
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { onEvent(PinUiEvent.ChangePin) },
                        modifier = Modifier
                            .padding(horizontal = pagePadding)
                            .fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Outlined.Done, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            stringResource(
                                if (state.hasPinSet != true) {
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
