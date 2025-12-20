package com.jackappsdev.password_manager.presentation.screens.password_item_detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Card

@Composable
fun DetailContentView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        onClick = {},
        enabled = false,
        modifier = modifier
    ) {
        content.invoke()
    }
}
