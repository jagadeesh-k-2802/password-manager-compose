package com.jackappsdev.password_manager.data.local

import androidx.room.ColumnInfo

data class PasswordCategory(
    val id: Int = 0,
    val name: String,
    val username: String,
    val password: String,
    val notes: String,
    val website: String,
    @ColumnInfo("is_added_to_watch") val isAddedToWatch: Boolean,
    @ColumnInfo("category_id") val categoryId: Int? = null,
    @ColumnInfo("category_name") val categoryName: String? = null,
    @ColumnInfo("category_color") val categoryColor: String? = null,
    @ColumnInfo("created_at") val createdAt: Long = System.currentTimeMillis()
)
