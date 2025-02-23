package com.jackappsdev.password_manager.domain.repository

/**
 * [DatabaseManagerRepository] is used to import and export the database.
 * - Supports SQLite DB import and export.
 */
interface DatabaseManagerRepository {
    suspend fun importDatabase(path: String, password: String): Boolean
    suspend fun exportDatabase(path: String)
}
