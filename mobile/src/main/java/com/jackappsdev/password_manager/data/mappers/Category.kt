package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.entity.CategoryEntity
import com.jackappsdev.password_manager.domain.model.CategoryModel

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
