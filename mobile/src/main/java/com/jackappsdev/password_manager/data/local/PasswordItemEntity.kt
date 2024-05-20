package com.jackappsdev.password_manager.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_items")
data class PasswordItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "website") val website: String,
    @ColumnInfo(name = "is_added_to_watch") val isAddedToWatch: Boolean,
    @ColumnInfo(name = "category_id") val categoryId: Int? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
