package com.jackappsdev.password_manager.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PasswordWithCategoryEntity(
    @Embedded val passwordItem: PasswordItemEntity,
    @Relation(parentColumn = "category_id", entityColumn = "id")
    val categoryEntity: CategoryEntity
)
