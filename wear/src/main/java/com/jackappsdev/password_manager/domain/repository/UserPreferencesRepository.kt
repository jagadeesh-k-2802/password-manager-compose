package com.jackappsdev.password_manager.domain.repository

interface UserPreferencesRepository {
    fun getPin(): String?
    suspend fun setPin(newPin: String?)
    suspend fun verifyPin(pin: String): Boolean
    suspend fun hasPinSet(): Boolean
}
