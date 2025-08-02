package com.jackappsdev.password_manager.shared.core

import android.content.Context
import net.zetetic.database.DatabaseErrorHandler
import net.zetetic.database.sqlcipher.SQLiteConnection
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SQLiteDatabaseHook
import net.zetetic.database.sqlcipher.SQLiteOpenHelper

class EncryptedSQLiteOpenHelper(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int,
    minimumSupportedVersion: Int,
    sqLiteDatabaseHook: SQLiteDatabaseHook = defaultSqLiteDatabaseHook,
    errorHandler: DatabaseErrorHandler = defaultDatabaseErrorHandler,
    password: String,
    enableWriteAheadLogging: Boolean = true
) : SQLiteOpenHelper(
    context,
    name,
    password,
    factory,
    version,
    minimumSupportedVersion,
    errorHandler,
    sqLiteDatabaseHook,
    enableWriteAheadLogging
) {
    override fun onCreate(db: SQLiteDatabase) {
        // No Operation
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // No Operation
    }
}

val defaultDatabaseErrorHandler = DatabaseErrorHandler { dbObj, exception ->
    println("Database error occurred: ${exception.message}")
}

val defaultSqLiteDatabaseHook = object : SQLiteDatabaseHook {
    override fun preKey(connection: SQLiteConnection?) {
        // No Operation
    }

    override fun postKey(connection: SQLiteConnection?) {
        // No Operation
    }
}
