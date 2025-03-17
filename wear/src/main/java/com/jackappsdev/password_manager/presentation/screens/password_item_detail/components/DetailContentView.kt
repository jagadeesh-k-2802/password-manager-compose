package com.jackappsdev.password_manager.presentation.screens.password_item_detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.MaterialTheme

@Composable
fun DetailContentView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        onClick = {},
        enabled = false,
        modifier = modifier,
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = MaterialTheme.colors.surface,
            endBackgroundColor = MaterialTheme.colors.surface,
        )
    ) {
        content.invoke()
    }
}
