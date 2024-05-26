package com.jackappsdev.password_manager.domain.repository

/**
 * [PassphraseRepository] - used only to [updatePin] from dataStore
 * [UserPreferencesRepository] was previously used but resulted in Hilt dependency cycle error
 */
interface PassphraseRepository {
    suspend fun updatePin(newPin: String)
}
