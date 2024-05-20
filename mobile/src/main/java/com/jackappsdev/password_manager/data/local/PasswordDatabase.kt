package com.jackappsdev.password_manager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

const val DATABASE_NAME = "passwords_db"
const val DATABASE_VERSION = 2

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE password_items ADD COLUMN website TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE password_items ADD COLUMN is_added_to_watch INT NOT NULL DEFAULT 0")
    }
}

@Database(
    entities = [PasswordItemEntity::class, CategoryEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
    abstract fun categoryDao(): CategoryDao
}
