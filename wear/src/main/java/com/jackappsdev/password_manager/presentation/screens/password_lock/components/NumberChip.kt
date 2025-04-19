package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.presentation.theme.isLargeDisplay

@Composable
fun NumberChip(
    modifier: Modifier = Modifier,
    label: String,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    val numButtonPadding = if (isLargeDisplay()) 20.dp else 18.dp

    Chip(
        onClick = { onEvent(PasswordLockUiEvent.NumberPress(label)) },
        modifier = modifier,
        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = numButtonPadding),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.title2
            )
        }
    )
}
