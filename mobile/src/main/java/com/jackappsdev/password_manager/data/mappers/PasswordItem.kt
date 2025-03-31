package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity
import com.jackappsdev.password_manager.domain.model.PasswordItemModel

fun PasswordItemEntity.toModel(): PasswordItemModel {
    return PasswordItemModel(
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
}

fun PasswordItemModel.toEntity(): PasswordItemEntity {
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
