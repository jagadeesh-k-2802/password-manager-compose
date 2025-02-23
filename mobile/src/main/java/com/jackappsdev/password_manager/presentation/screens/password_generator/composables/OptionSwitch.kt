package com.jackappsdev.password_manager.presentation.screens.password_generator.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@Composable
fun OptionSwitch(
    title: String,
    isItemChecked: Boolean,
    onItemCheckChange: ((Boolean) -> Unit)
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemCheckChange(!isItemChecked) }
    ) {
        Row {
            Spacer(modifier = Modifier.width(pagePadding))
            Text(title)
        }

        Row {
            Switch(checked = isItemChecked, onCheckedChange = onItemCheckChange)
            Spacer(modifier = Modifier.width(pagePadding))
        }
    }
}
