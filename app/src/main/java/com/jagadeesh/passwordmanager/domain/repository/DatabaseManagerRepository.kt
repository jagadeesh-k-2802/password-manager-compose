package com.jagadeesh.passwordmanager.domain.repository

interface DatabaseManagerRepository {
    suspend fun importData(path: String)
    suspend fun exportData(path: String)
}
