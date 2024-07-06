package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository

/**
 * [PassphraseRepository] - used only to [updatePin] from [dataStore]
 * [UserPreferencesRepository] was previously used but resulted in Hilt dependency cycle error
 */
class PassphraseRepositoryImpl(
    private val dataStore: DataStore<UserSettings>
) : PassphraseRepository {
    override suspend fun updatePin(newPin: String) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(pin = newPin) }
    }
}
