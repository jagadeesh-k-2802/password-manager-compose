package com.jackappsdev.password_manager.presentation.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.jackappsdev.password_manager.BuildConfig
import com.jackappsdev.password_manager.constants.DEFAULT_APP_AUTO_LOCK_DELAY
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val passwordLockViewModel: PasswordLockViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private var autoLockStartTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            passwordLockViewModel.state.hasPasswordSet == null &&
            mainViewModel.useDynamicColors == null
        }

        // Disable autofill for the entire app
        window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS

        // Disable screenshots & screen recordings for production builds
        if (BuildConfig.DEBUG.not()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        setContent {
            PasswordManagerTheme(dynamicColor = mainViewModel.useDynamicColors == true) {
                val navController = rememberNavController()
                Router(navController, passwordLockViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkForAutoLock()
    }

    override fun onPause() {
        super.onPause()
        saveCurrentTimeMillis()
    }

    private fun saveCurrentTimeMillis() {
        autoLockStartTimeMillis = System.currentTimeMillis()
    }

    private fun checkForAutoLock() {
        val millisElapsedSinceOnPause = System.currentTimeMillis() - autoLockStartTimeMillis
        val delay = mainViewModel.autoLockDelayMs ?: DEFAULT_APP_AUTO_LOCK_DELAY
        if (millisElapsedSinceOnPause >= delay) { passwordLockViewModel.setUnlocked(false) }
    }
}
