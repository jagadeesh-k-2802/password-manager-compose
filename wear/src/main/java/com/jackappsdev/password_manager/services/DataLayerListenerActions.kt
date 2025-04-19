package com.jackappsdev.password_manager.services

interface DataLayerListenerActions {
    suspend fun setPin(newPin: String?)
    suspend fun upsertPasswordItem(serializedPasswordItem: String?)
    suspend fun deletePasswordItem(serializedPasswordItem: String?)
    suspend fun onWipeDataPath()
}
