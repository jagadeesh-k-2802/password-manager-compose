package com.jackappsdev.password_manager.services

/**
 * Interface for the [DataLayerListenerService] to interact with the [DataLayerListenerActions]
 */
interface DataLayerListenerActions {
    suspend fun setPin(newPin: String?)
    suspend fun upsertPasswordItem(serializedPasswordItem: String?)
    suspend fun deletePasswordItem(serializedPasswordItem: String?)
    suspend fun onWipeDataPath()
}
