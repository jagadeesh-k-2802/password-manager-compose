package com.jackappsdev.password_manager.domain.repository

interface PassphraseRepository {
    suspend fun updatePassword(newPassword: String)
}
