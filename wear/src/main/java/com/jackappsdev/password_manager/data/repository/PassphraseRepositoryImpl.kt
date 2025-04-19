package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository

/**
 * [PassphraseRepository] - used only to [updatePin] in [dataStore]
 */
class PassphraseRepositoryImpl(
    private val dataStore: DataStore<UserSettings>
) : PassphraseRepository {

    override suspend fun updatePin(newPin: String) {
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(pin = newPin) }
    }
}
