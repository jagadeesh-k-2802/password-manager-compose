package com.jagadeesh.passwordmanager.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

fun generateRandomPassword(
    length: Int,
    includeLowercase: Boolean = true,
    includeUppercase: Boolean = true,
    includeNumbers: Boolean = true,
    includeSymbols: Boolean = true,
    additionalCharacters: String? = null
): String {
    @Suppress("SpellCheckingInspection")
    val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
    @Suppress("SpellCheckingInspection")
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

fun copyToClipboard(context: Context, text: String?, label: String = "Text"): Boolean {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
    return true
}

fun <T> debounce(
    delayMs: Long = 500L,
    coroutineContext: CoroutineContext,
    f: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null

    return { param: T ->
        if (debounceJob?.isCompleted != false) {
            debounceJob = CoroutineScope(coroutineContext).launch {
                delay(delayMs)
                f(param)
            }
        }
    }
}

fun parseColor(string: String): Color {
    val hexString = string.removePrefix("#")
    val red = hexString.substring(0, 2).toInt(16)
    val green = hexString.substring(2, 4).toInt(16)
    val blue = hexString.substring(4, 6).toInt(16)
    return Color(red, green, blue)
}
