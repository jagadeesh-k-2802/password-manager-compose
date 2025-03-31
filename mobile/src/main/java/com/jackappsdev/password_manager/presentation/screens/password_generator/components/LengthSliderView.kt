package com.jackappsdev.password_manager.presentation.screens.password_generator.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorState
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@Composable
fun LengthSliderView(
    state: PasswordGeneratorState,
    onEvent: (PasswordGeneratorUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = pagePadding)
    ) {
        Text(stringResource(R.string.text_length, state.passwordLength))

        Slider(
            value = state.passwordLength.toFloat(),
            valueRange = 6f..32f,
            onValueChange = { onEvent(PasswordGeneratorUiEvent.OnLengthChange(it.toInt())) },
        )
    }
}
