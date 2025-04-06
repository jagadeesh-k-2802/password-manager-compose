package com.jackappsdev.password_manager.presentation.screens.change_password.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordError
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordState
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEvent

@Composable
fun ChangePasswordView(
    error: ChangePasswordError?,
    state: ChangePasswordState,
    onEvent: (ChangePasswordUiEvent) -> Unit,
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
                    imageVector = if (state.showNewPassword) {
                        Icons.Outlined.VisibilityOff
                    } else {
                        Icons.Outlined.Visibility
                    },
                    contentDescription = stringResource(R.string.accessibility_toggle_new_password)
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
