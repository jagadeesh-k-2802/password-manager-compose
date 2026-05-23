package com.jackappsdev.password_manager.core

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD_ID
import com.jackappsdev.password_manager.shared.constants.REMOVE_PASSWORD_FROM_WATCH
import com.jackappsdev.password_manager.shared.constants.REQUEST_VERSION
import com.jackappsdev.password_manager.shared.constants.VERSION_INFO
import com.jackappsdev.password_manager.shared.core.VersionTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Class responsible for managing wearable communication (version information exchange and
 * data layer updates like removing passwords from watch) between the mobile and Wear OS devices.
 */
class MobileWearableManager(
    private val context: Context,
    private val passwordItemRepository: PasswordItemRepository
) : MessageClient.OnMessageReceivedListener, DataClient.OnDataChangedListener {

    private val messageClient = Wearable.getMessageClient(context)
    private val dataClient = Wearable.getDataClient(context)
    private val nodeClient = Wearable.getNodeClient(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    fun register() {
        messageClient.addListener(this)
        dataClient.addListener(this)
        requestAndSendVersion()
    }

    fun unregister() {
        messageClient.removeListener(this)
        dataClient.removeListener(this)
    }

    fun requestAndSendVersion() {
        scope.launch {
            try {
                val nodes = nodeClient.connectedNodes.await()
                val currentVersion = VersionTracker.getAppVersionName(context)
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

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            REQUEST_VERSION -> {
                scope.launch {
                    try {
                        val currentVersion = VersionTracker.getAppVersionName(context)
                        messageClient.sendMessage(
                            messageEvent.sourceNodeId,
                            VERSION_INFO,
                            currentVersion.toByteArray(Charsets.UTF_8)
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            VERSION_INFO -> {
                val version = String(messageEvent.data, Charsets.UTF_8)
                VersionTracker.otherDeviceVersion = version
            }
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { dataEvent ->
            if (dataEvent.type == DataEvent.TYPE_CHANGED) {
                val uri = dataEvent.dataItem.uri
                when (uri.path) {
                    REMOVE_PASSWORD_FROM_WATCH -> {
                        try {
                            val map = DataMapItem.fromDataItem(dataEvent.dataItem).dataMap
                            val passwordId = map.getInt(KEY_PASSWORD_ID, -1)
                            if (passwordId != -1) {
                                scope.launch {
                                    passwordItemRepository.updateWatchStatus(passwordId, false)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}
