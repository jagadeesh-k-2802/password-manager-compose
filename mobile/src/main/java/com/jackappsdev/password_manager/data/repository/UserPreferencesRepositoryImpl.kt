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

    override fun getPassword(): String? {
        return runBlocking(Dispatchers.IO) { dataStore.data.first().password }
    }

    override suspend fun setInitialPassword(newPassword: String) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(password = newPassword) }
    }

    override suspend fun hasPasswordSet(): Boolean {
        return dataStore.data.first().password.isNullOrBlank().not()
    }

    override suspend fun verifyPassword(password: String): Boolean {
        return password == dataStore.data.first().password
    }

    override suspend fun getScreenLockToUnlock(): Boolean {
        return dataStore.data.first().useScreenLockToUnlock
    }

    override suspend fun setUseScreenLockToUnlock(newValue: Boolean) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(useScreenLockToUnlock = newValue) }
    }

    override suspend fun getUseDynamicColors(): Flow<Boolean> {
        return dataStore.data.map { it.useDynamicColors }
    }

    override suspend fun setUseDynamicColors(newValue: Boolean) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(useDynamicColors = newValue) }
    }

    override suspend fun hasAndroidWatchPinSet(): Boolean {
        return dataStore.data.first().androidWatchPin.isNullOrBlank().not()
    }

    override suspend fun setAndroidWatchPinSet(newPin: String?) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(androidWatchPin = newPin) }
    }
}
