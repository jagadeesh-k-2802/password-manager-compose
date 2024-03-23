package com.jagadeesh.passwordmanager.data.mappers

import com.jagadeesh.passwordmanager.data.local.PasswordItemEntity
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel

fun PasswordItemEntity.toModel(): PasswordItemModel {
    return PasswordItemModel(
        id = id,
        name = name,
        username = username,
        password = password,
        notes = notes,
        categoryId = categoryId,
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
