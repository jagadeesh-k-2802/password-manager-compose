package com.jackappsdev.password_manager.presentation.screens.settings.event

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_BIOMETRIC_ENROLL
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import androidx.activity.result.ActivityResultLauncher
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat.getString
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.isAtLeastAndroid
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.shared.constants.PLAY_STORE_APP_URI
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsEffectHandler(
    private val activity: FragmentActivity,
    private val navController: NavController,
    private val scope: CoroutineScope,
    private val onEvent: (SettingsUiEvent) -> Unit,
) {
    private val context: Context = activity.applicationContext

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(context, R.string.title_verify_user))
        .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    private val biometricPrompt = BiometricPrompt(
        activity,
        activity.mainExecutor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onEvent(SettingsUiEvent.ToggleUseScreenLock)
            }
        }
    )

    fun onStartAppUpdate(appUpdateManager: AppUpdateManager) {
        scope.launch {
            val appUpdateInfo = appUpdateManager.requestAppUpdateInfo()
            val appUpdateOptions = AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
            appUpdateManager.startUpdateFlow(appUpdateInfo, activity, appUpdateOptions)
        }
    }

    fun onOpenImportPasswordsIntent(intent: ActivityResultLauncher<Intent>) {
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            intent.launch(this)
        }
    }

    fun onOpenExportPasswordsIntent(intent: ActivityResultLauncher<Intent>) {
        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            type = "application/vnd.sqlite3"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, "passwords.db")
            intent.launch(this)
        }
    }

    fun onPasswordsExported() {
        context.showToast(context.getString(R.string.toast_passwords_exported))
    }

    fun onBiometricAuthenticate() {
        biometricPrompt.authenticate(promptInfo)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun onOpenScreenLockSettings() {
        context.showToast(context.getString(R.string.toast_setup_lock_screen))

        val intent = if (isAtLeastAndroid(Build.VERSION_CODES.R)) {
            Intent(ACTION_BIOMETRIC_ENROLL).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        } else {
            Intent(ACTION_SECURITY_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun onNavigateToChangePassword() {
        navController.navigate(Routes.ChangePassword)
    }

    fun onNavigateToManageCategories() {
        navController.navigate(Routes.ManageCategories)
    }

    fun onNavigateToAndroidWatch() {
        navController.navigate(Routes.AndroidWatch)
    }

    fun onOpenPlayStorePage() {
        val intent = try {
            Intent(Intent.ACTION_VIEW).apply {
                data = "market://details?id=${context.packageName}".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        } catch (_: Exception) {
            Intent(Intent.ACTION_VIEW).apply {
                data = PLAY_STORE_APP_URI.toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        context.startActivity(intent)
    }
}
