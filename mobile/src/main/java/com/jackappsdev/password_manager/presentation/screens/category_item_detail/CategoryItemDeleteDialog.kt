package com.jackappsdev.password_manager.presentation.screens.category_item_detail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun CategoryItemDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Category") },
        text = { Text("Do you want to delete this category ?") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Yes") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
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
