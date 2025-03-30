package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent

@Composable
fun BackSpaceChip(
    modifier: Modifier = Modifier,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    Chip(
        onClick = { onEvent(PasswordLockUiEvent.OnBackSpacePress) },
        modifier = modifier,
        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 12.dp),
        label = { },
        icon = {
            Icon(
                Icons.AutoMirrored.Default.Backspace,
                modifier = Modifier.size(16.dp),
                contentDescription = stringResource(R.string.accessibility_backspace)
            )
        }
    )
}
