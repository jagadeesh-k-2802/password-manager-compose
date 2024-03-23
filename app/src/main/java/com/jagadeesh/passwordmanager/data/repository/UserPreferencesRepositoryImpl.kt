package com.jagadeesh.passwordmanager.data.repository

import androidx.datastore.core.DataStore
import com.jagadeesh.passwordmanager.data.models.UserSettings
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
        dataStore.updateData { prevUserSettings ->
            UserSettings(
                password = newPassword,
                useScreenLockToUnlock = prevUserSettings.useScreenLockToUnlock
            )
        }
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
        dataStore.updateData { prevUserSettings ->
            UserSettings(
                password = prevUserSettings.password,
                useScreenLockToUnlock = newValue
            )
        }
    }
}
