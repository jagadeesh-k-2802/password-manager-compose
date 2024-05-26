package com.jackappsdev.password_manager.services

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.MainActivity
import com.jackappsdev.password_manager.shared.constants.DELETE_PASSWORD
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.KEY_PIN
import com.jackappsdev.password_manager.shared.constants.SET_PIN_PATH
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import com.jackappsdev.password_manager.shared.data.dto.PasswordItemDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var passphraseRepository: PassphraseRepository

    @Inject
    lateinit var passwordItemRepository: PasswordItemRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @SuppressLint("WearRecents")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri

            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (uri.path) {
                        SET_PIN_PATH -> {
                            DataMapItem.fromDataItem(dataEvent.dataItem).dataMap.apply {
                                scope.launch {
                                    getString(KEY_PIN)?.let { newPin ->
                                        if (userPreferencesRepository.hasPinSet()) {
                                            // Update existing PIN & Database
                                            passphraseRepository.updatePin(newPin)
                                        } else {
                                            // Create NEW PIN
                                            userPreferencesRepository.setPin(newPin)
                                        }
                                    }
                                }.invokeOnCompletion {
                                    Intent(
                                        applicationContext,
                                        MainActivity::class.java
                                    ).addFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                    ).apply {
                                        startActivity(this)
                                    }
                                }
                            }
                        }

                        UPSERT_PASSWORD -> {
                            DataMapItem.fromDataItem(dataEvent.dataItem).dataMap.apply {
                                scope.launch {
                                    if (userPreferencesRepository.hasPinSet()) {
                                        getString(KEY_PASSWORD)?.let { serializedString ->
                                            passwordItemRepository.insertPasswordItem(
                                                Json.decodeFromString<PasswordItemDto>(
                                                    serializedString
                                                ).toPasswordItemModel()
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        DELETE_PASSWORD -> {
                            DataMapItem.fromDataItem(dataEvent.dataItem).dataMap.apply {
                                scope.launch {
                                    if (userPreferencesRepository.hasPinSet()) {
                                        getString(KEY_PASSWORD)?.let { serializedString ->
                                            passwordItemRepository.deletePasswordItem(
                                                Json.decodeFromString<PasswordItemDto>(
                                                    serializedString
                                                ).toPasswordItemModel()
                                            )
                                        }
                                    }
                                }
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
