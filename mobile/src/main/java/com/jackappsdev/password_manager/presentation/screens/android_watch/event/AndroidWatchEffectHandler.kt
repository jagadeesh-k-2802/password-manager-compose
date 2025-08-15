package com.jackappsdev.password_manager.presentation.screens.android_watch.event

import android.content.Context
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import com.google.android.gms.wearable.CapabilityClient.FILTER_REACHABLE
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.constants.KEY_PIN
import com.jackappsdev.password_manager.shared.constants.SET_PIN
import com.jackappsdev.password_manager.shared.constants.VERIFY_WEAR_APP
import com.jackappsdev.password_manager.shared.constants.WIPE_DATA
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AndroidWatchEffectHandler(
    private val context: Context,
    private val navController: NavController,
    private val scope: CoroutineScope,
    private val keyboardController: SoftwareKeyboardController?,
    private val onEvent: (AndroidWatchUiEvent) -> Unit,
) {

    private val dataClient = Wearable.getDataClient(context)

    fun onRequestPinChange() {
        checkIfWatchIsAvailableAndAppInstalled {
            onEvent(AndroidWatchUiEvent.SetupPin)
        }
    }

    fun onSetupPin(pin: String) {
        keyboardController?.hide()

        val putDataRequest = PutDataMapRequest.create(SET_PIN).run {
            // Adding time part of data to allow setting the same PIN within short span of time
            dataMap.putString(KEY_PIN, "$pin ${System.currentTimeMillis()}")
            setUrgent()
            asPutDataRequest()
        }

        dataClient.putDataItem(putDataRequest).addOnSuccessListener {
            context.showToast(context.getString(R.string.toast_watch_pin_set))
            navController.navigateUp()
        }
    }

    fun onConfirmToggleAndroidWatch() {
        checkIfWatchIsAvailableAndAppInstalled {
            onEvent(AndroidWatchUiEvent.ToggleAndroidWatch)
        }
    }

    fun onDisableWatchSharing() {
        val putDataRequest = PutDataMapRequest.create(WIPE_DATA).run {
            dataMap.putString(KEY_PIN, System.currentTimeMillis().toString())
            setUrgent()
            asPutDataRequest()
        }

        dataClient.putDataItem(putDataRequest).addOnCompleteListener {
            context.showToast(context.getString(R.string.toast_android_watch_disabled))
            navController.navigateUp()
        }
    }

    fun onNavigateUp() {
        navController.navigateUp()
    }

    /**
     * Check if Wear OS Watch is connected & app is installed
     */
    private fun checkIfWatchIsAvailableAndAppInstalled(onComplete: () -> Unit) {
        var isWatchConnectedAndAppInstalled = false
        val nodeClient = Wearable.getNodeClient(context)
        val capabilityClient = Wearable.getCapabilityClient(context)

        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (nodeClient.connectedNodes.await().isNotEmpty()) {
                        val capability = capabilityClient.getCapability(VERIFY_WEAR_APP, FILTER_REACHABLE)
                        isWatchConnectedAndAppInstalled = capability.await().nodes.isNotEmpty()
                    }
                } catch (e: Exception) {
                    println(e)
                    // Do Nothing
                }
            }
        }.invokeOnCompletion {
            if (!isWatchConnectedAndAppInstalled) {
                context.showToast(context.getString(R.string.toast_connect_watch))
            } else {
                onComplete()
            }
        }
    }
}
