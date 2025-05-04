package com.jackappsdev.password_manager.presentation.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.jackappsdev.password_manager.BuildConfig
import com.jackappsdev.password_manager.constants.APP_AUTO_LOCK_DELAY
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val passwordLockViewModel: PasswordLockViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private var autoLockJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            passwordLockViewModel.state.hasPasswordSet == null &&
            mainViewModel.useDynamicColors == null
        }

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
        cancelAutoLockJob()
    }

    override fun onPause() {
        super.onPause()
        startAutoLockJob()
    }

    fun startAutoLockJob() {
        if (autoLockJob?.isActive == true) {
            autoLockJob?.cancel()
        }

        autoLockJob = lifecycleScope.launch {
            delay(APP_AUTO_LOCK_DELAY)
            passwordLockViewModel.setUnlocked(false)
        }
    }

    fun cancelAutoLockJob() {
        if (autoLockJob?.isActive == true) {
            autoLockJob?.cancel()
        }
    }
}
