package com.jackappsdev.password_manager.presentation.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.Text
import com.jackappsdev.password_manager.presentation.screens.base.WearPreview
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun PasswordItem(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    label: String,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        icon = icon?.let { { Icon(painter = icon, contentDescription = null) } },
        label = { Text(text = label, maxLines = maxLines) }
    )
}

@WearPreview
@Composable
fun PasswordItemPreview() {
    PasswordManagerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PasswordItem(
                label = "Google",
                onClick = { },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
