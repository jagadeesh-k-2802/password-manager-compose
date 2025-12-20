package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent

@Composable
fun BackSpaceChip(
    modifier: Modifier = Modifier,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    IconButton(
        onClick = { onEvent(PasswordLockUiEvent.BackSpacePress) },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Backspace,
            modifier = Modifier.size(16.dp),
            contentDescription = stringResource(R.string.accessibility_backspace)
        )
    }
}
