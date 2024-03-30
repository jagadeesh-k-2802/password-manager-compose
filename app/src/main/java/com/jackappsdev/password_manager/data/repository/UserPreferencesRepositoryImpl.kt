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
    override fun getPassword(): String? {
        return runBlocking(Dispatchers.IO) {
            return@runBlocking dataStore.data.first().password
        }
    }

    /**
     * Set password for the first time only
     */
    override suspend fun setPassword(newPassword: String) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(password = newPassword) }
    }

    /**
     * Check whether user has a password already
     */
    override suspend fun hasPasswordSet(): Boolean {
        val userSettings = dataStore.data.first()
        return !userSettings.password.isNullOrBlank()
    }

    /**
     * Verify password when unlocking
     */
    override suspend fun verifyPassword(password: String): Boolean {
        val prevPassword = dataStore.data.first().password
        return password == prevPassword
    }

    /**
     * Check whether user has enabled unlock using screen lock
     */
    override suspend fun getScreenLockToUnlock(): Boolean {
        return dataStore.data.first().useScreenLockToUnlock
    }

    /**
     * Set useScreenLockToUnlock preference according to user
     */
    override suspend fun setUseScreenLockToUnlock(newValue: Boolean) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(useScreenLockToUnlock = newValue) }
    }

    override suspend fun getUseDynamicColors(): Flow<Boolean> {
        return dataStore.data.map { it.useDynamicColors }
    }

    override suspend fun setUseDynamicColors(newValue: Boolean) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(useDynamicColors = newValue) }
    }
}
