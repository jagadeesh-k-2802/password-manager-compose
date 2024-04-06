package com.jackappsdev.password_manager.presentation.theme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable

/**
 * Colors which makes disabled [OutlinedTextField] look like normal [OutlinedTextField]
 */
@Composable
fun disabledButEnabledOutlinedTextFieldColors(): TextFieldColors {
    val defaultColors = OutlinedTextFieldDefaults.colors()
    
    return OutlinedTextFieldDefaults.colors(
        disabledContainerColor = defaultColors.unfocusedContainerColor,
        disabledLabelColor = defaultColors.unfocusedLabelColor,
        disabledTextColor = defaultColors.unfocusedTextColor,
        disabledBorderColor = defaultColors.unfocusedTextColor,
        disabledTrailingIconColor = defaultColors.unfocusedTrailingIconColor
    )
}
