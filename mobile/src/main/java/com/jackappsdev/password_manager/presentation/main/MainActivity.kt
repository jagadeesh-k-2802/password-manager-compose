package com.jackappsdev.password_manager.presentation.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jackappsdev.password_manager.constants.DEFAULT_APP_AUTO_LOCK_DELAY
import com.jackappsdev.password_manager.core.BaseActivity
import com.jackappsdev.password_manager.presentation.navigation.LocalResultEventBus
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.ResultEventBus
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.navigation.TOP_LEVEL_ROUTES
import com.jackappsdev.password_manager.presentation.navigation.rememberNavigationState
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
            val navigationState = rememberNavigationState(startRoute = Routes.PasswordLock, topLevelRoutes = TOP_LEVEL_ROUTES)
            val navigator = remember { Navigator(navigationState) }
            val resultEventBus = remember { ResultEventBus() }

            PasswordManagerTheme(dynamicColor = mainViewModel.useDynamicColors == true) {
                CompositionLocalProvider(LocalResultEventBus provides resultEventBus) {
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
                            navigator = navigator,
                            navigationState = navigationState,
                            passwordLockViewModel = passwordLockViewModel
                        )
                    }
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
