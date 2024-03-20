package com.jagadeesh.passwordmanager.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.jagadeesh.passwordmanager.presentation.navigation.Router
import com.jagadeesh.passwordmanager.presentation.screens.password_lock.PasswordLockViewModel
import com.jagadeesh.passwordmanager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val passwordLockViewModel: PasswordLockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { passwordLockViewModel.state.hasPasswordSet == null }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()
                Router(navController, passwordLockViewModel)
            }
        }
    }
}
