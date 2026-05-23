package com.jackappsdev.password_manager.presentation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.shared.constants.REQUEST_VERSION
import com.jackappsdev.password_manager.shared.constants.VERSION_INFO
import com.jackappsdev.password_manager.shared.core.VersionTracker
import com.jackappsdev.password_manager.presentation.navigation.Router
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val passwordLockViewModel: PasswordLockViewModel by viewModels()

    /**
     * If PIN is updated, then close the activity
     */
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
        sendVersionAndRequestMobileVersion()

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

    private fun sendVersionAndRequestMobileVersion() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val nodeClient = Wearable.getNodeClient(applicationContext)
                val messageClient = Wearable.getMessageClient(applicationContext)
                val nodes = nodeClient.connectedNodes.await()
                val currentVersion = VersionTracker.getAppVersionName(applicationContext)
                val versionBytes = currentVersion.toByteArray(Charsets.UTF_8)
                for (node in nodes) {
                    messageClient.sendMessage(node.id, REQUEST_VERSION, byteArrayOf())
                    messageClient.sendMessage(node.id, VERSION_INFO, versionBytes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
    }

    private fun registerReceiver() {
        ContextCompat.registerReceiver(
            baseContext,
            pinChangeBroadcastReceiver,
            IntentFilter(PIN_CHANGE_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(pinChangeBroadcastReceiver)
    }

    companion object {
        const val PIN_CHANGE_ACTION = "com.jackappsdev.password_manager.PIN_CHANGE"
    }
}
