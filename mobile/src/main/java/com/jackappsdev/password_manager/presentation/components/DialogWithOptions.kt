package com.jackappsdev.password_manager.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DialogWithOptions(
    @StringRes title: Int,
    options: List<OptionItem>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = title)) },
        text = {
            LazyColumn(
                content = {
                    itemsIndexed(options) { index, item ->
                        DialogOptionItem(
                            index = index,
                            selected = index == selectedIndex,
                            label = item.label,
                            onSelectIndex = onSelectIndex
                        )
                    }
                }
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text(text = stringResource(id = com.jackappsdev.password_manager.R.string.dialog_btn_ok)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text = stringResource(id = com.jackappsdev.password_manager.R.string.dialog_btn_cancel)) } }
    )
}

@Composable
private fun DialogOptionItem(
    modifier: Modifier = Modifier,
    index: Int,
    @StringRes label: Int,
    selected: Boolean,
    onSelectIndex: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onSelectIndex(index) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = { onSelectIndex(index) })
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(id = label))
    }
}

data class OptionItem(
    @StringRes val label: Int,
    val value: Long
)
