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
import com.jackappsdev.password_manager.presentation.components.InfoText
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordError
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordState
import com.jackappsdev.password_manager.presentation.screens.change_password.event.ChangePasswordUiEvent

@Composable
fun ChangePasswordView(
    state: ChangePasswordState,
    error: ChangePasswordError?,
    onEvent: (ChangePasswordUiEvent) -> Unit,
) {
    OutlinedTextField(
        value = state.currentPassword,
        onValueChange = { onEvent(ChangePasswordUiEvent.PasswordEnter(it)) },
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
            IconButton(onClick = { onEvent(ChangePasswordUiEvent.ToggleShowPasswordVisibility) }) {
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

    OutlinedTextField(
        value = state.newPassword,
        onValueChange = { onEvent(ChangePasswordUiEvent.NewPasswordEnter(it)) },
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
            IconButton(onClick = { onEvent(ChangePasswordUiEvent.ToggleShowNewPasswordVisibility) }) {
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

    Spacer(modifier = Modifier.height(4.dp))
    InfoText(text = stringResource(R.string.text_change_password_help))
    Spacer(modifier = Modifier.height(20.dp))

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onEvent(ChangePasswordUiEvent.UpdatePassword) }
    ) {
        Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_confirm))
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.btn_confirm))
    }
}
