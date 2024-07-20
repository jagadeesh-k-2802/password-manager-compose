package com.jackappsdev.password_manager.presentation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val passwordLockViewModel: PasswordLockViewModel by viewModels()

    private val pinChangeBroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("UnsafeIntentLaunch")
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == PIN_CHANGE_ACTION) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        splashScreen.setKeepOnScreenCondition {
            passwordLockViewModel.state.hasPinSet == null
        }

        setContent {
            PasswordManagerTheme {
                val navController = rememberSwipeDismissableNavController()
                Router(navController, passwordLockViewModel)
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(PIN_CHANGE_ACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(pinChangeBroadcastReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(pinChangeBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(pinChangeBroadcastReceiver)
    }

    companion object {
        const val PIN_CHANGE_ACTION = "com.jackappsdev.password_manager.PIN_CHANGE"
    }
}
