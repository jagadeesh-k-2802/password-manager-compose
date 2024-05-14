package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme

@Composable
fun PasswordItem(
    modifier: Modifier = Modifier,
    icon: @Composable (BoxScope.() -> Unit)? = null,
    label: @Composable (RowScope.() -> Unit),
    onClick: () -> Unit
) {
    Chip(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ChipDefaults.chipColors(backgroundColor = MaterialTheme.colors.surface),
        icon = icon,
        label = label
    )
}
