package com.jagadeesh.passwordmanager.domain.repository

interface DatabaseManagerRepository {
    suspend fun importData(path: String, password: String): Boolean
    suspend fun exportData(path: String)
}
