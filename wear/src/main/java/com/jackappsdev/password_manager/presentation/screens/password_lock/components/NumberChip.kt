package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.presentation.theme.isLargeDisplay

@Composable
fun NumberChip(
    modifier: Modifier = Modifier,
    label: String,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    val numButtonPadding = if (isLargeDisplay()) 22.dp else 18.dp

    FilledTonalButton(
        onClick = { onEvent(PasswordLockUiEvent.NumberPress(label)) },
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = numButtonPadding),
        shape = CircleShape,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    )
}
