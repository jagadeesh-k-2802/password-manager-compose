package com.jackappsdev.password_manager.presentation.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val passwordLockViewModel: PasswordLockViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            passwordLockViewModel.state.hasPasswordSet == null && mainViewModel.useDynamicColors == null
        }

        // Disable screenshots & screen recordings
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContent {
            PasswordManagerTheme(dynamicColor = mainViewModel.useDynamicColors == true) {
                val navController = rememberNavController()
                Router(navController, passwordLockViewModel)
            }
        }
    }
}
