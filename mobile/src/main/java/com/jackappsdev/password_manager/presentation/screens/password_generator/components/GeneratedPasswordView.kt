package com.jackappsdev.password_manager.presentation.screens.password_generator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorState
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@Composable
fun GeneratedPasswordView(
    state: PasswordGeneratorState,
    onEvent: (PasswordGeneratorUiEvent) -> Unit
) {
    SelectionContainer {
        Column(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 8.dp,
                start = pagePadding,
                end = pagePadding
            )
        ) {
            Text(
                text = state.password,
                style = MaterialTheme.typography.headlineLarge
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(state.passwordStrengthText),
                    color = state.passwordStrengthColor,
                    fontWeight = FontWeight.SemiBold
                )

                Row {
                    IconButton(onClick = { onEvent(PasswordGeneratorUiEvent.RegeneratePassword) }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(R.string.accessibility_generate_again)
                        )
                    }

                    IconButton(onClick = { onEvent(PasswordGeneratorUiEvent.CopyPassword) }) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = stringResource(R.string.accessibility_copy_text)
                        )
                    }
                }
            }
        }
    }
}
