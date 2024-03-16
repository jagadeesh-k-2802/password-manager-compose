package com.jagadeesh.passwordmanager.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import kotlin.random.Random

@Suppress("SpellCheckingInspection")
fun generateRandomPassword(
    length: Int,
    includeLowercase: Boolean = true,
    includeUppercase: Boolean = true,
    includeNumbers: Boolean = true,
    includeSymbols: Boolean = true,
    additionalCharacters: String? = null
): String {
    val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
    val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val numberChars = "0123456789"
    val symbolChars = "!@#$%^&*()-_+=[]{}|;:,.<>?/`~"

    var availableChars = ""
    if (includeLowercase) availableChars += lowercaseChars
    if (includeUppercase) availableChars += uppercaseChars
    if (includeNumbers) availableChars += numberChars
    if (includeSymbols) availableChars += symbolChars
    additionalCharacters?.let { availableChars += it }

    if (availableChars.isEmpty()) {
        throw IllegalArgumentException("At least one character set must be included")
    }

    return buildString {
        repeat(length) {
            append(availableChars[Random.nextInt(availableChars.length)])
        }
    }
}

fun copyToClipboard(context: Context, text: String, label: String = "Password"): Boolean {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    return true
}
