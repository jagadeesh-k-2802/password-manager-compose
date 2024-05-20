package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.PasswordCategory
import com.jackappsdev.password_manager.data.local.PasswordItemEntity
import com.jackappsdev.password_manager.domain.model.PasswordCategoryModel

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
        website = website,
        isAddedToWatch = isAddedToWatch,
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
            website = website,
            isAddedToWatch = isAddedToWatch,
            createdAt = createdAt
        )
    } else {
        PasswordItemEntity(
            name = name,
            username = username,
            notes = notes,
            password = password,
            website = website,
            isAddedToWatch = isAddedToWatch,
            categoryId = categoryId
        )
    }
}
