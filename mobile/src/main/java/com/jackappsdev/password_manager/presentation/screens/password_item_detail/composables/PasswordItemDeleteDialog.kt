package com.jackappsdev.password_manager.presentation.screens.password_item_detail.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R

@Composable
fun PasswordItemDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.dialog_title_delete_password)) },
        text = { Text(stringResource(R.string.dialog_text_password_delete)) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.dialog_btn_yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.dialog_btn_cancel)) } }
    )
}
