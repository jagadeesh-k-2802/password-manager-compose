package com.jackappsdev.password_manager.presentation.screens.category_item_detail.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import com.jackappsdev.password_manager.R

@Composable
fun CategoryItemDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_title_delete_category)) },
        text = { Text(stringResource(R.string.dialog_text_category_delete)) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.dialog_btn_yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.dialog_btn_cancel)) } }
    )
}

@Preview
@Composable
private fun CategoryItemDeleteDialogPreview() {
    PasswordManagerTheme {
        CategoryItemDeleteDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
