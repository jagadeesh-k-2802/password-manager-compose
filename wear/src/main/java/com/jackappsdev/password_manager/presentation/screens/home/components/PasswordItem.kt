package com.jackappsdev.password_manager.presentation.screens.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun PasswordItem(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    label: String,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    Chip(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ChipDefaults.chipColors(backgroundColor = MaterialTheme.colors.surface),
        icon = icon?.let {
            { Icon(painter = icon, contentDescription = null) }
        },
        label = { Text(text = label, maxLines = maxLines) }
    )
}

@Preview
@Composable
fun PasswordItemPreview() {
    PasswordManagerTheme {
        PasswordItem(
            label = "Google",
            onClick = { }
        )
    }
}
