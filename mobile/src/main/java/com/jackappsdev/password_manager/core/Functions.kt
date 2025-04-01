package com.jackappsdev.password_manager.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.ui.graphics.Color
import com.jackappsdev.password_manager.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import androidx.core.net.toUri
import com.jackappsdev.password_manager.shared.core.showToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    context.showToast(context.getString(R.string.toast_copy_to_clipboard))
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
    } catch (_: Exception) {
        context.showToast(context.getString(R.string.toast_invalid_url))
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
 *
 */
fun parseModifiedTime(context: Context, time: Long): String {
    val is24Hours = DateFormat.is24HourFormat(context)
    val hours = if (is24Hours) "HH" else "hh"
    val dateFormat = SimpleDateFormat("dd/MM/yyyy $hours:mm", Locale.ENGLISH)
    return dateFormat.format(Date(time))
}

/**
 * Check if the device is running Android version greater than or equal to [sdkVersion]
 */
fun isAtLeastAndroid(sdkVersion: Int): Boolean {
    return Build.VERSION.SDK_INT >= sdkVersion
}

/**
 * Check if the device has screen lock setup
 */
fun isScreenLockAvailable(context: Context): Boolean {
    val manager = BiometricManager.from(context)
    val credentials = BIOMETRIC_WEAK or BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    return manager.canAuthenticate(credentials) == BiometricManager.BIOMETRIC_SUCCESS
}
