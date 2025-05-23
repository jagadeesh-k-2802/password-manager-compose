package com.jackappsdev.password_manager.presentation.screens.password_generator

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class PasswordGeneratorState(
    val password: String = EMPTY_STRING,
    val passwordLength: Int = 8,
    val includeLowercase: Boolean = true,
    val includeUppercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSymbols: Boolean = true,
    @StringRes val passwordStrengthText: Int = R.string.text_weak,
    val passwordStrengthColor: Color = Color.Transparent,
    val passwordStrengthColorDark: Color = Color.Transparent
)
