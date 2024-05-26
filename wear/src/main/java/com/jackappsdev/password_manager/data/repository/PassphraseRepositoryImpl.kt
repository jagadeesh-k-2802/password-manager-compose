package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.local.PasswordDao
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first

/**
 * [PassphraseRepository] - used only to [updatePin] from [dataStore]
 * [UserPreferencesRepository] was previously used but resulted in Hilt dependency cycle error
 */
class PassphraseRepositoryImpl(
    private val passwordDao: PasswordDao,
    private val dataStore: DataStore<UserSettings>
) : PassphraseRepository {
    /**
     * Updates the Sqlite DB password also
     */
    override suspend fun updatePin(newPin: String) {
        val oldPin = dataStore.data.first().pin
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(pin = newPin) }
        passwordDao.changePassword(oldPin, newPin)
    }
}
