package com.jackappsdev.password_manager.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun UnsavedChangesDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_title_unsaved_changes)) },
        text = { Text(stringResource(R.string.dialog_text_unsaved)) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.dialog_btn_yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.dialog_btn_cancel)) } }
    )
}

@Preview
@Composable
private fun UnsavedChangesDialogPreview() {
    PasswordManagerTheme {
        UnsavedChangesDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
