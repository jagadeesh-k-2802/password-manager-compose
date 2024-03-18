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
    override fun getPassword(): String? {
        return runBlocking(Dispatchers.IO) {
            return@runBlocking dataStore.data.first().password
        }
    }

    override suspend fun setPassword(newPassword: String) {
        dataStore.updateData { UserSettings(password = newPassword) }
    }

    override suspend fun hasPasswordSet(): Boolean {
        val userSettings = dataStore.data.first()
        return !userSettings.password.isNullOrBlank()
    }

    override suspend fun verifyPassword(password: String): Boolean {
        val prevUserSettings = dataStore.data.first()
        return password == prevUserSettings.password
    }
}
