package com.jackappsdev.password_manager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM password_items")
    fun getAllPasswordEntities(): Flow<List<PasswordItemEntity>>

    @Query("SELECT * FROM password_items WHERE id = :id")
    fun getPasswordEntity(id: Int): Flow<PasswordItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordEntity(item: PasswordItemEntity)

    @Delete
    suspend fun deletePasswordEntity(item: PasswordItemEntity)

    @RawQuery
    suspend fun executeQuery(query: SimpleSQLiteQuery): List<Any>

    @Query("DELETE FROM password_items")
    fun deleteAllPasswords()
}
