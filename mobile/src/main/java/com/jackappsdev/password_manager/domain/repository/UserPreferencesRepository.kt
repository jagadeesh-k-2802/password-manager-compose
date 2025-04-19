package com.jackappsdev.password_manager.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getPassword(): String?
    suspend fun setInitialPassword(newPassword: String)
    suspend fun hasPasswordSet(): Boolean
    suspend fun verifyPassword(password: String): Boolean
    suspend fun getScreenLockToUnlock(): Boolean
    suspend fun setUseScreenLockToUnlock(newValue: Boolean)
    suspend fun getUseDynamicColors(): Flow<Boolean>
    suspend fun setUseDynamicColors(newValue: Boolean)
    suspend fun hasAndroidWatchPinSet(): Boolean
    suspend fun setAndroidWatchPinSet(newPin: String?)
}
