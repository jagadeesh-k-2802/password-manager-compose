package com.jackappsdev.password_manager.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.jackappsdev.password_manager.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import androidx.core.net.toUri
import kotlin.random.Random

/**
 * Generate a random password based on the configuration
 */
fun generateRandomPassword(config: GeneratePasswordConfig): String {
    @Suppress("SpellCheckingInspection")
    val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"

    @Suppress("SpellCheckingInspection")
    val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val numberChars = "0123456789"
    val symbolChars = "!@#$%^&*()-_+=[]{}|;:,.<>?/`~"

    var availableChars = ""
    if (config.includeLowercase) availableChars += lowercaseChars
    if (config.includeUppercase) availableChars += uppercaseChars
    if (config.includeNumbers) availableChars += numberChars
    if (config.includeSymbols) availableChars += symbolChars
    config.additionalCharacters?.let { availableChars += it }

    if (availableChars.isEmpty()) {
        throw IllegalArgumentException("At least one character set must be included")
    }

    return buildString {
        repeat(config.length) {
            append(availableChars[Random.nextInt(availableChars.length)])
        }
    }
}

/**
 * Copy the [text] to clipboard
 */
fun copyToClipboard(context: Context, text: String?, label: String = "Text") {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(context, context.getString(R.string.toast_copy_to_clipboard), Toast.LENGTH_SHORT).show()
}

/**
 * Launch URL in browser
 */
fun launchUrl(context: Context, url: String) {
    var currUrl = url
    if (!url.startsWith("http://") && !url.startsWith("https://")) currUrl = "http://$url"

    try {
        val intent = Intent(Intent.ACTION_VIEW, currUrl.toUri())
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.toast_invalid_url), Toast.LENGTH_SHORT)
            .show()
    }
}

/**
 * Debounce function to prevent multiple calls in a short period of time
 */
fun <T> debounce(delayMs: Long = 500L, coroutineContext: CoroutineContext, f: (T) -> Unit): (T) -> Unit {
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

/**
 * Parse [String] Hex color to [Color]
 */
fun parseColor(string: String): Color {
    val hexString = string.removePrefix("#")
    val red = hexString.substring(0, 2).toInt(16)
    val green = hexString.substring(2, 4).toInt(16)
    val blue = hexString.substring(4, 6).toInt(16)
    return Color(red, green, blue)
}

/**
 * Check if the device is running Android version greater than or equal to [sdkVersion]
 */
fun isAndroid(sdkVersion: Int): Boolean {
    return Build.VERSION.SDK_INT >= sdkVersion
}
