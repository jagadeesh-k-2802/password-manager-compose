package com.jackappsdev.password_manager.presentation.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.jackappsdev.password_manager.constants.DEFAULT_APP_AUTO_LOCK_DELAY
import com.jackappsdev.password_manager.core.BaseActivity
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@OptIn(ExperimentalComposeUiApi::class)
class MainActivity : BaseActivity() {

    private var autoLockStartTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            passwordLockViewModel.state.hasPasswordSet == null &&
            mainViewModel.useDynamicColors == null
        }

        setContent {
            val navController = rememberNavController()

            PasswordManagerTheme(dynamicColor = mainViewModel.useDynamicColors == true) {
                InterceptPlatformTextInput(
                    interceptor = { request, nextHandler ->
                        handlePlatformTextInput(
                            request,
                            nextHandler,
                            mainViewModel.useIncognitoKeyboard == true
                        )
                    }
                ) {
                    Router(
                        navController = navController,
                        passwordLockViewModel = passwordLockViewModel
                    )
                }
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
