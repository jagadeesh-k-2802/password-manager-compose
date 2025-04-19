package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.components.InfoText
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockError
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockState
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@Composable
fun SetupPasswordView(
    state: PasswordLockState,
    error: PasswordLockError?,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    Spacer(modifier = Modifier.height(pagePadding))

    OutlinedTextField(
        value = state.password,
        onValueChange = { onEvent(PasswordLockUiEvent.EnterPassword(it)) },
        label = { Text(stringResource(R.string.label_password)) },
        modifier = Modifier.fillMaxWidth(),
        isError = error is PasswordLockError.PasswordError,
        visualTransformation = if (state.showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        supportingText = {
            error?.let {
                if (it is PasswordLockError.PasswordError) Text(stringResource(it.error))
            }
        },
        trailingIcon = {
            IconButton(onClick = { onEvent(PasswordLockUiEvent.ToggleShowPasswordVisibility) }) {
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )

    OutlinedTextField(
        value = state.confirmPassword,
        onValueChange = { onEvent(PasswordLockUiEvent.EnterConfirmPassword(it)) },
        label = { Text(stringResource(R.string.label_confirm_password)) },
        modifier = Modifier.fillMaxWidth(),
        isError = error is PasswordLockError.ConfirmPasswordError,
        visualTransformation = if (state.showConfirmPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        supportingText = {
            error?.let {
                if (it is PasswordLockError.ConfirmPasswordError) Text(stringResource(it.error))
            }
        },
        trailingIcon = {
            IconButton(onClick = { onEvent(PasswordLockUiEvent.ToggleShowConfirmPasswordVisibility) }) {
                Icon(
                    imageVector = if (state.showConfirmPassword) {
                        Icons.Outlined.VisibilityOff
                    } else {
                        Icons.Outlined.Visibility
                    },
                    contentDescription = stringResource(R.string.accessibility_toggle_confirm_password)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )

    InfoText(
        modifier = Modifier.padding(vertical = pagePadding),
        text = stringResource(R.string.text_password_warning_note)
    )
    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = { onEvent(PasswordLockUiEvent.SetupNewPassword) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_confirm))
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.btn_confirm))
    }
}
