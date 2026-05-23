package com.jackappsdev.password_manager.presentation.screens.password_item_detail.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.AlertDialogDefaults
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.shared.core.VersionTracker

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun ItemView(
    passwordItem: PasswordItemModel?,
    columnState: ScalingLazyColumnState,
    onConfirmRemove: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val otherVersion = VersionTracker.otherDeviceVersion
    val showRemoveButton = VersionTracker.isVersionAtLeast(otherVersion, "2.11.0")

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        columnState = columnState
    ) {
        item {
            ListHeader { Text(text = passwordItem?.name ?: "...") }
        }

        item {
            DetailContentView {
                Text(stringResource(R.string.label_username), fontWeight = FontWeight.Bold)
                Text("${passwordItem?.username}")

                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(R.string.label_password), fontWeight = FontWeight.Bold)
                Text("${passwordItem?.password}")
            }
        }

        if (passwordItem?.notes?.isNotBlank() == true) {
            item {
                DetailContentView {
                    Text(stringResource(R.string.label_notes), fontWeight = FontWeight.Bold)
                    Text(passwordItem.notes)
                }
            }
        }

        if (showRemoveButton) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(imageVector = Icons.Default.Close, contentDescription = null) },
                    label = { Text(text = stringResource(R.string.btn_remove)) }
                )
            }
        }
    }

    AlertDialog(
        visible = showDialog,
        onDismissRequest = { showDialog = false },
        title = { Text(text = stringResource(R.string.title_remove_password)) },
        text = { Text(text = stringResource(R.string.text_remove_watch_only_desc)) },
        confirmButton = {
            AlertDialogDefaults.ConfirmButton(
                onClick = {
                    showDialog = false
                    onConfirmRemove()
                }
            )
        },
        dismissButton = {
            AlertDialogDefaults.DismissButton(
                onClick = { showDialog = false }
            )
        }
    )
}
