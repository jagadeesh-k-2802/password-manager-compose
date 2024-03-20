package com.jagadeesh.passwordmanager.presentation.screens.item_detail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ItemDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Password Item") },
        text = { Text("Do you want to delete this password ?") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Yes") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
