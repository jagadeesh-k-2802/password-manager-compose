package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockError
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockState
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent

@Composable
fun EnterPasswordView(
    state: PasswordLockState,
    error: PasswordLockError?,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    LaunchedEffect(state.hasPasswordSet, state.useScreenLockToUnlock) {
        if (state.hasPasswordSet == true && state.useScreenLockToUnlock == true) {
            onEvent(PasswordLockUiEvent.BiometricAuthenticate)
        }
    }

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
        keyboardActions = KeyboardActions(
            onDone = { onEvent(PasswordLockUiEvent.VerifyPassword) }
        )
    )

    Spacer(modifier = Modifier.height(4.dp))

    Button(
        onClick = { onEvent(PasswordLockUiEvent.VerifyPassword) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_confirm))
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.btn_confirm))
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (state.useScreenLockToUnlock == true && state.isScreenLockAvailable == true) {
        OutlinedButton(
            onClick = { onEvent(PasswordLockUiEvent.BiometricAuthenticate) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Outlined.LockOpen, stringResource(R.string.accessibility_use_device_lock_screen))
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.btn_use_device_lock_screen))
        }
    }
}
