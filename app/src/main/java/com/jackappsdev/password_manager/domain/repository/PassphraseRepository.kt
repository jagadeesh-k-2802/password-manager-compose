package com.jackappsdev.password_manager.domain.repository

/**
 * [PassphraseRepository] - used only to [updatePassword] from dataStore
 * [UserPreferencesRepository] was previously used but resulted in Hilt dependency cycle error
 */
interface PassphraseRepository {
    suspend fun updatePassword(newPassword: String)
}
