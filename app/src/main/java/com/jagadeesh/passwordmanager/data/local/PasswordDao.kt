package com.jagadeesh.passwordmanager.data.local

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
    @RawQuery(observedEntities = [PasswordItemEntity::class])
    fun getAllPasswordEntities(query: SimpleSQLiteQuery): Flow<List<PasswordItemEntity>>

    @Query("SELECT * FROM password_items WHERE id = :id")
    fun getPasswordEntity(id: Int): Flow<PasswordItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordEntity(item: PasswordItemEntity)

    @RawQuery
    suspend fun executeQuery(query: SimpleSQLiteQuery): List<Any>

    suspend fun changePassword(oldPassword: String?, newPassword: String) {
        executeQuery(SimpleSQLiteQuery("PRAGMA key = '$oldPassword'"))
        executeQuery(SimpleSQLiteQuery("PRAGMA rekey = '$newPassword'"))
    }

    @Delete(entity = PasswordItemEntity::class)
    suspend fun deletePasswordEntity(item: PasswordItemEntity)
}
