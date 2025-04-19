package com.jackappsdev.password_manager.core

data class GeneratePasswordConfig(
    val length: Int,
    val includeLowercase: Boolean = true,
    val includeUppercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSymbols: Boolean = true,
    val additionalCharacters: String? = null
)
