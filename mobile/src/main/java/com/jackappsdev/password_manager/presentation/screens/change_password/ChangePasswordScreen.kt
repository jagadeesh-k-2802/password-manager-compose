package com.jackappsdev.password_manager.presentation.screens.change_password

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordEffectHandler
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEffect
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
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
                when(effect) {
                    ChangePasswordUiEffect.OnPasswordChanged -> onPasswordChanged()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_change_password)) },
                windowInsets = windowInsetsVerticalZero
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
            Text(stringResource(R.string.text_change_password_help))
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.currentPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.OnPasswordEnter(it)) },
                label = { Text(stringResource(R.string.label_current_password)) },
                modifier = Modifier.fillMaxWidth(),
                isError = error is ChangePasswordError.CurrentPasswordError,
                supportingText = {
                    error?.let {
                        if (it is ChangePasswordError.CurrentPasswordError) Text(stringResource(it.error))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                visualTransformation = if (state.showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardActions = KeyboardActions(onDone = {}),
                trailingIcon = {
                    IconButton(onClick = { onEvent(ChangePasswordUiEvent.ToggleShowPassword) }) {
                        Icon(
                            imageVector = if (state.showPassword) {
                                Icons.Outlined.VisibilityOff
                            } else {
                                Icons.Outlined.Visibility
                            },
                            contentDescription = stringResource(R.string.accessibility_toggle_password)
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = state.newPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.OnNewPasswordEnter(it)) },
                label = { Text(stringResource(R.string.label_new_password)) },
                modifier = Modifier.fillMaxWidth(),
                isError = error is ChangePasswordError.NewPasswordError,
                supportingText = {
                    error?.let {
                        if (it is ChangePasswordError.NewPasswordError) Text(stringResource(it.error))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                visualTransformation = if (state.showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardActions = KeyboardActions(onDone = {}),
                trailingIcon = {
                    IconButton(onClick = { onEvent(ChangePasswordUiEvent.ToggleShowNewPassword) }) {
                        Icon(
                            imageVector = if (state.showPassword) {
                                Icons.Outlined.VisibilityOff
                            } else {
                                Icons.Outlined.Visibility
                            },
                            contentDescription = stringResource(R.string.accessibility_toggle_password)
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(ChangePasswordUiEvent.OnPasswordChanged) }
            ) {
                Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_confirm))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_confirm))
            }
        }
    }
}
