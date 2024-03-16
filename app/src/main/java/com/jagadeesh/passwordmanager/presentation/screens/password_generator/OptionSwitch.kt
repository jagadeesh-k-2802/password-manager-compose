package com.jagadeesh.passwordmanager.presentation.screens.password_generator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OptionSwitch(
    title: String,
    isItemChecked: Boolean,
    onItemCheckChange: ((Boolean) -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title)
        Switch(checked = isItemChecked, onCheckedChange = onItemCheckChange)
    }
}
