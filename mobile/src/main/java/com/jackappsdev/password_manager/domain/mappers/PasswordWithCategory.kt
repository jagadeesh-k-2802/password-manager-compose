package com.jackappsdev.password_manager.domain.mappers

import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.data.dto.PasswordItemDto

fun PasswordWithCategoryModel.toPasswordItemDto(): PasswordItemDto {
    return PasswordItemDto(
        id = id ?: 0,
        name = name,
        username = username,
        password = password,
        notes = notes,
        createdAt = createdAt ?: 0
    )
}

fun PasswordWithCategoryModel.toCategoryModel(): CategoryModel {
    return CategoryModel(
        id = categoryId,
        name = categoryName ?: "",
        color = categoryColor ?: ""
    )
}
