package com.jagadeesh.passwordmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM password_items ORDER BY :orderBy")
    fun getPasswordEntities(orderBy: String): Flow<List<PasswordItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordEntity(item: PasswordItemEntity)

    @Delete(entity = PasswordItemEntity::class)
    suspend fun deletePasswordEntity(item: PasswordItemEntity)
}
