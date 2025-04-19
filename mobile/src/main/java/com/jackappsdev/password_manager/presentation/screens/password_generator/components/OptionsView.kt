package com.jackappsdev.password_manager.presentation.screens.password_generator.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorState
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent

@Composable
fun OptionsView(
    state: PasswordGeneratorState,
    onEvent: (PasswordGeneratorUiEvent) -> Unit
) {
    OptionSwitch(
        title = stringResource(R.string.label_include_lowercase),
        isItemChecked = state.includeLowercase,
        onItemCheckChange = { onEvent(PasswordGeneratorUiEvent.ToggleIncludeLowercase) }
    )

    OptionSwitch(
        title = stringResource(R.string.label_include_uppercase),
        isItemChecked = state.includeUppercase,
        onItemCheckChange = { onEvent(PasswordGeneratorUiEvent.ToggleIncludeUppercase) }
    )

    OptionSwitch(
        title = stringResource(R.string.label_include_numbers),
        isItemChecked = state.includeNumbers,
        onItemCheckChange = { onEvent(PasswordGeneratorUiEvent.ToggleIncludeNumbers) }
    )

    OptionSwitch(
        title = stringResource(R.string.label_include_symbols),
        isItemChecked = state.includeSymbols,
        onItemCheckChange = { onEvent(PasswordGeneratorUiEvent.ToggleIncludeSymbols) }
    )
}
