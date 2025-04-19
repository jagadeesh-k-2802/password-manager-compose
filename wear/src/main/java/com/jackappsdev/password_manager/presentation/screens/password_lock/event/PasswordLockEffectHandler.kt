package com.jackappsdev.password_manager.presentation.screens.password_lock.event

import android.content.Context
import android.content.Intent
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.core.net.toUri
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.constants.PLAY_STORE_APP_URI
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors

class PasswordLockEffectHandler(
    private val context: Context,
    private val scope: CoroutineScope,
    private val hapticFeedback: HapticFeedback
) {

    fun onUnlock() {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    fun onIncorrectPassword() {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        context.showToast(context.getString(R.string.toast_incorrect_password))
    }

    fun onOpenPhoneApp() {
        val remoteActivityHelper = RemoteActivityHelper(
            context,
            Executors.newSingleThreadExecutor()
        )

        val intent = Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(PLAY_STORE_APP_URI.toUri())

        scope.launch {
            try {
                val nodeClient = Wearable.getNodeClient(context)
                val node = nodeClient.connectedNodes.await().first()
                remoteActivityHelper.startRemoteActivity(intent, node.id)
                context.showToast(context.getString(R.string.toast_opened_phone_app))
            } catch (_: Exception) {
                context.showToast(context.getString(R.string.toast_cannot_open_phone_app))
            }
        }
    }
}
