package com.jackappsdev.password_manager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_NAME = "passwords_db"
const val DATABASE_VERSION = 1

@Database(
    entities = [PasswordItemEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}
