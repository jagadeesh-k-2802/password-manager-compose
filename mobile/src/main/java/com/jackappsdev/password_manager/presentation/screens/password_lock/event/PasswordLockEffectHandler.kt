package com.jackappsdev.password_manager.presentation.screens.password_lock.event

import android.content.Context
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.FragmentActivity
import com.jackappsdev.password_manager.R

class PasswordLockEffectHandler(
    activity: FragmentActivity,
    private val keyboardController: SoftwareKeyboardController?,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    private val context: Context = activity.applicationContext

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(context, R.string.title_biometric))
        .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    private val biometricPrompt = BiometricPrompt(
        activity,
        activity.mainExecutor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onEvent(PasswordLockUiEvent.SetUnlocked(true))
            }
        }
    )

    fun onHideKeyboard() {
        keyboardController?.hide()
    }

    fun onBiometricAuthenticate() {
        biometricPrompt.authenticate(promptInfo)
    }
}
