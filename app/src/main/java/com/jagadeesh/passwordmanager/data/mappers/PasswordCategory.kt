package com.jagadeesh.passwordmanager.data.mappers

import com.jagadeesh.passwordmanager.data.local.PasswordCategory
import com.jagadeesh.passwordmanager.data.local.PasswordItemEntity
import com.jagadeesh.passwordmanager.domain.model.PasswordCategoryModel

fun PasswordCategory.toModel(): PasswordCategoryModel {
    return PasswordCategoryModel(
        id = id,
        name = name,
        username = username,
        password = password,
        notes = notes,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryColor = categoryColor,
        createdAt = createdAt
    )
}

fun PasswordCategoryModel.toPasswordItemEntity(): PasswordItemEntity {
    return if (id != null && createdAt != null) {
        PasswordItemEntity(
            id = id,
            name = name,
            username = username,
            password = password,
            notes = notes,
            categoryId = categoryId,
            createdAt = createdAt
        )
    } else {
        PasswordItemEntity(
            name = name,
            username = username,
            notes = notes,
            password = password,
            categoryId = categoryId
        )
    }
}
