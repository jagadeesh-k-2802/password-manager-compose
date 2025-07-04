package com.jackappsdev.password_manager.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

@Composable
fun PasswordInputDialog(
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes label: Int,
    isInvalidPassword: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf(EMPTY_STRING) }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = password,
                    label = { Text(stringResource(label)) },
                    isError = isInvalidPassword,
                    supportingText = { if (isInvalidPassword) Text(stringResource(R.string.error_password_does_not_match)) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { value -> password = value }
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(description))
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(password) }) { Text(stringResource(R.string.dialog_btn_confirm)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.dialog_btn_cancel)) } }
    )
}

@Preview
@Composable
private fun ImportPasswordInputDialogPreview() {
    PasswordManagerTheme {
        PasswordInputDialog(
            title = R.string.dialog_title_import_passwords,
            description = R.string.text_import_passwords_note,
            label = R.string.label_database_password,
            isInvalidPassword = false,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ExportCsvPasswordInputDialogPreview() {
    PasswordManagerTheme {
        PasswordInputDialog(
            title = R.string.dialog_title_export_passwords_as_csv,
            description = R.string.text_export_passwords_csv_note,
            label = R.string.label_password,
            isInvalidPassword = false,
            onConfirm = {},
            onDismiss = {}
        )
    }
}
