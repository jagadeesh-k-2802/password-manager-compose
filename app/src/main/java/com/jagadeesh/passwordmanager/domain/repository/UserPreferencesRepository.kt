package com.jagadeesh.passwordmanager.domain.repository

interface UserPreferencesRepository {
    fun getPassword(): String?
    suspend fun setPassword(newPassword: String)
    suspend fun hasPasswordSet(): Boolean
    suspend fun verifyPassword(password: String): Boolean
    suspend fun getScreenLockToUnlock(): Boolean
    suspend fun setUseScreenLockToUnlock(newValue: Boolean)
}
