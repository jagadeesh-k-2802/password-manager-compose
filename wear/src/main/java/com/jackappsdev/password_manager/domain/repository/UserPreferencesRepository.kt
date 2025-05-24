package com.jackappsdev.password_manager.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getPin(): String?
    fun listenForPin(): Flow<String?>
    suspend fun setPin(newPin: String?)
    suspend fun verifyPin(pin: String): Boolean
    suspend fun hasPinSet(): Boolean
}
