package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.local.dao.PasswordDao
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import kotlinx.coroutines.flow.first

/**
 * [PassphraseRepository] - used only to [updatePassword] in [dataStore] and [passwordDao]
 */
class PassphraseRepositoryImpl(
    private val passwordDao: PasswordDao,
    private val dataStore: DataStore<UserSettings>
) : PassphraseRepository {

    override suspend fun updatePassword(newPassword: String) {
        val oldPassword = dataStore.data.first().password
        dataStore.updateData { prevUserSettings -> prevUserSettings.copy(password = newPassword) }
        passwordDao.changePassword(oldPassword, newPassword)
    }
}
