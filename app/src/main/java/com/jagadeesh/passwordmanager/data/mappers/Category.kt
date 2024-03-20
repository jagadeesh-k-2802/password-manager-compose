package com.jagadeesh.passwordmanager.data.mappers

import com.jagadeesh.passwordmanager.data.local.CategoryEntity
import com.jagadeesh.passwordmanager.domain.model.CategoryModel

fun CategoryEntity.toModel(): CategoryModel {
    return CategoryModel(
        id = id,
        name = name,
        color = color,
        createdAt = createdAt
    )
}

fun CategoryModel.toEntity(): CategoryEntity {
    return if (id != null && createdAt != null) {
        CategoryEntity(
            id = id,
            name = name,
            color = color,
            createdAt = createdAt
        )
    } else {
        CategoryEntity(
            name = name,
            color = color,
        )
    }
}
