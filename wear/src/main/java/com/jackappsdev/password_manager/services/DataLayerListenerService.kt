package com.jackappsdev.password_manager.services

import android.annotation.SuppressLint
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.WearableListenerService
import com.jackappsdev.password_manager.core.getDataMap
import com.jackappsdev.password_manager.shared.constants.DELETE_PASSWORD
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.KEY_PIN
import com.jackappsdev.password_manager.shared.constants.SET_PIN
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import com.jackappsdev.password_manager.shared.constants.WIPE_DATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {

    @Inject
    lateinit var dataLayerListenerActions: DataLayerListenerActions

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @SuppressLint("WearRecents")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri

            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    val map = dataEvent.getDataMap()
                    scope.launch {
                        with(dataLayerListenerActions) {
                            when (uri.path) {
                                SET_PIN -> setPin(map.getString(KEY_PIN))
                                UPSERT_PASSWORD -> upsertPasswordItem(map.getString(KEY_PASSWORD))
                                DELETE_PASSWORD -> deletePasswordItem(map.getString(KEY_PASSWORD))
                                WIPE_DATA -> onWipeDataPath()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
