package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
fun DoneChip(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    Chip(
        onClick = { onEvent(PasswordLockUiEvent.VerifyPin) },
        enabled = enabled,
        modifier = modifier,
        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 12.dp),
        label = { },
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                modifier = Modifier.size(18.dp),
                contentDescription = stringResource(R.string.accessibility_done)
            )
        }
    )
}
