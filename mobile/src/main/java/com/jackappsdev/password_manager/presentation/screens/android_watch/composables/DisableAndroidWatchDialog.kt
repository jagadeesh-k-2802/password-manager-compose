package com.jackappsdev.password_manager.presentation.screens.android_watch.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R

@Composable
fun DisableAndroidWatchDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.dialog_title_disable_android_watch)) },
        text = { Text(stringResource(R.string.dialog_text_disable_android_watch)) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.dialog_btn_yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.dialog_btn_cancel)) } }
    )
}
