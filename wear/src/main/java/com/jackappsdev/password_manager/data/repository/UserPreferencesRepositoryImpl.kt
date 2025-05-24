package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<UserSettings>
) : UserPreferencesRepository {

    /**
     * Return the password for DB encryption purpose
     */
    override fun getPin(): String? {
        return runBlocking(Dispatchers.IO) { dataStore.data.first().pin }
    }

    /**
     * Listen for pin changes using flow
     */
    override fun listenForPin(): Flow<String?> {
        return dataStore.data.map { data -> data.pin }
    }

    /**
     * Set password for the first time only
     */
    override suspend fun setPin(newPin: String?) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(pin = newPin) }
    }

    /**
     * Verify pin when unlocking
     */
    override suspend fun verifyPin(pin: String): Boolean {
        return pin == dataStore.data.first().pin
    }

    /**
     * Check whether user has a password already
     */
    override suspend fun hasPinSet(): Boolean {
        return dataStore.data.first().pin.isNullOrBlank().not()
    }
}
