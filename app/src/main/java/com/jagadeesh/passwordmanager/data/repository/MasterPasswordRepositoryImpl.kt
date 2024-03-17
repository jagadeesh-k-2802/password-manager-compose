package com.jagadeesh.passwordmanager.data.repository

import androidx.datastore.core.DataStore
import com.jagadeesh.passwordmanager.core.CryptoManager
import com.jagadeesh.passwordmanager.data.models.UserSettings
import com.jagadeesh.passwordmanager.domain.repository.MasterPasswordRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class MasterPasswordRepositoryImpl(
    private val dataStore: DataStore<UserSettings>
) : MasterPasswordRepository {
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
