package com.jackappsdev.password_manager.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jackappsdev.password_manager.shared.constants.ZERO

@Entity(tableName = "password_items")
data class PasswordItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = ZERO,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
