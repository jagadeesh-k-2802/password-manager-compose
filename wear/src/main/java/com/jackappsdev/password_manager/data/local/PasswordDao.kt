package com.jackappsdev.password_manager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM password_items")
    fun getAllPasswordEntities(): Flow<List<PasswordItemEntity>>

    @Query("SELECT * FROM password_items WHERE id = :id")
    fun getPasswordEntity(id: Int): Flow<PasswordItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPasswordEntity(item: PasswordItemEntity)

    @Delete
    suspend fun deletePasswordEntity(item: PasswordItemEntity)

    @Query("DELETE FROM password_items")
    fun deleteAllPasswords()
}
