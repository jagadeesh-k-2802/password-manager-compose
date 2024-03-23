package com.jagadeesh.passwordmanager.data.repository

import androidx.datastore.core.DataStore
import com.jagadeesh.passwordmanager.data.local.PasswordDao
import com.jagadeesh.passwordmanager.data.models.UserSettings
import com.jagadeesh.passwordmanager.domain.repository.PassphraseRepository
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first

/**
 * [PassphraseRepository] - used only to [updatePassword] from [dataStore]
 * [UserPreferencesRepository] was previously used but resulted in Hilt dependency cycle error
 */
class PassphraseRepositoryImpl(
    private val passwordDao: PasswordDao,
    private val dataStore: DataStore<UserSettings>
) : PassphraseRepository {
    /**
     * Updates the Sqlite DB password also
     */
    override suspend fun updatePassword(newPassword: String) {
        val oldPassword = dataStore.data.first().password

        dataStore.updateData { prevUserSettings ->
            UserSettings(
                password = newPassword,
                useScreenLockToUnlock = prevUserSettings.useScreenLockToUnlock
            )
        }

        passwordDao.changePassword(oldPassword, newPassword)
    }
}
