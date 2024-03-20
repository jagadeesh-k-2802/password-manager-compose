package com.jagadeesh.passwordmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_NAME = "passwords_db"
const val DATABASE_VERSION = 1

@Database(
    entities = [PasswordItemEntity::class, CategoryEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
    abstract fun categoryDao(): CategoryDao
}
