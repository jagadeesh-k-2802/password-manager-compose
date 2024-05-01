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
    @RawQuery(observedEntities = [PasswordItemEntity::class])
    fun getAllPasswordEntities(query: SimpleSQLiteQuery): Flow<List<PasswordItemEntity>>

    @Query("SELECT DISTINCT username FROM password_items WHERE username LIKE :username LIMIT :limit")
    suspend fun getUniqueUsernames(username: String, limit: Int): List<String>

    @Query(
        "SELECT password_items.id AS id, " +
                "password_items.name AS name, " +
                "password_items.username AS username, " +
                "password_items.password AS password, " +
                "password_items.notes AS notes, " +
                "categories.id AS category_id, " +
                "categories.name AS category_name, " +
                "categories.color AS category_color, " +
                "password_items.created_at AS created_at " +
                "FROM password_items " +
                "LEFT JOIN categories ON password_items.category_id = categories.id " +
                "WHERE password_items.id = :id"
    )
    fun getPasswordEntity(id: Int): Flow<PasswordCategory?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordEntity(item: PasswordItemEntity)

    @RawQuery
    suspend fun executeQuery(query: SimpleSQLiteQuery): List<Any>

    suspend fun changePassword(oldPassword: String?, newPassword: String) {
        executeQuery(SimpleSQLiteQuery("PRAGMA key = '$oldPassword'"))
        executeQuery(SimpleSQLiteQuery("PRAGMA rekey = '$newPassword'"))
    }

    suspend fun checkpoint() {
        executeQuery(SimpleSQLiteQuery("PRAGMA wal_checkpoint"))
    }

    @Delete
    suspend fun deletePasswordEntity(item: PasswordItemEntity)
}
