package com.jackappsdev.password_manager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity
import com.jackappsdev.password_manager.data.local.entity.PasswordWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @RawQuery(observedEntities = [PasswordItemEntity::class])
    fun getAllPasswordEntities(query: SimpleSQLiteQuery): Flow<List<PasswordItemEntity>>

    @Query("SELECT DISTINCT username FROM password_items WHERE username LIKE :username LIMIT :limit")
    suspend fun getUniqueUsernames(username: String, limit: Int): List<String>

    @Transaction
    @Query("SELECT * FROM password_items WHERE id = :id")
    fun getPasswordItem(id: Int): Flow<PasswordWithCategoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPasswordEntity(item: PasswordItemEntity)

    @Query("UPDATE password_items SET is_added_to_watch = 0")
    suspend fun removePasswordsFromWatch()

    @Query("UPDATE password_items SET category_id = NULL WHERE category_id = :id")
    suspend fun removeCategoryFromPasswords(id: Int)

    @Delete
    suspend fun deletePasswordEntity(item: PasswordItemEntity)

    @Query("SELECT * FROM password_items ORDER BY name ASC")
    suspend fun getAllPasswordWithCategoryEntities(): List<PasswordWithCategoryEntity>

    @RawQuery
    suspend fun executeQuery(query: SimpleSQLiteQuery): List<Any>

    suspend fun changePassword(oldPassword: String?, newPassword: String) {
        executeQuery(SimpleSQLiteQuery("PRAGMA key = '$oldPassword'"))
        executeQuery(SimpleSQLiteQuery("PRAGMA rekey = '$newPassword'"))
    }

    suspend fun checkpoint() {
        executeQuery(SimpleSQLiteQuery("PRAGMA wal_checkpoint"))
    }
}
