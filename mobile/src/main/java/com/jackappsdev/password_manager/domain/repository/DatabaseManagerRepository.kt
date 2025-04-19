package com.jackappsdev.password_manager.domain.repository

interface DatabaseManagerRepository {
    suspend fun importDatabase(path: String, password: String): Boolean
    suspend fun exportDatabase(path: String)
}
