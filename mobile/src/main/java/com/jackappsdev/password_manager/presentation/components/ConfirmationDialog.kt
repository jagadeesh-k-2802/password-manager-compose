package com.jackappsdev.password_manager.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun ConfirmationDialog(
    @StringRes title: Int,
    @StringRes description: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(description)) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.dialog_btn_yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.dialog_btn_cancel)) } }
    )
}

@Preview
@Composable
private fun DeleteCategoryConfirmationDialogPreview() {
    PasswordManagerTheme {
        ConfirmationDialog(
            title = R.string.dialog_title_delete_category,
            description = R.string.dialog_text_category_delete,
            onConfirm = {},
            onDismiss = {}
        )
    }
}
