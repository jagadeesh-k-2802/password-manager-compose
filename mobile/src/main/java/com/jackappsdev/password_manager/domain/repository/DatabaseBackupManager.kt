package com.jackappsdev.password_manager.domain.repository

interface DatabaseBackupManager {
    suspend fun importDatabase(path: String, password: String): Boolean
    suspend fun exportDatabase(path: String)
    suspend fun exportCsv(path: String)
}
