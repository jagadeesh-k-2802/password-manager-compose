package com.jackappsdev.password_manager.domain.repository

interface PassphraseRepository {
    suspend fun updatePin(newPin: String)
}
