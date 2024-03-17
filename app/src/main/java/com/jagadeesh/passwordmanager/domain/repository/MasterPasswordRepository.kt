package com.jagadeesh.passwordmanager.domain.repository

interface MasterPasswordRepository {
    suspend fun setPassword(newPassword: String)
    suspend fun hasPasswordSet(): Boolean
    suspend fun verifyPassword(password: String): Boolean
}
