package com.jackappsdev.password_manager.domain.mappers

import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
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

fun PasswordWithCategoryModel.toCategoryModel(): CategoryModel? {
    return if (categoryId != null) {
        CategoryModel(
            id = categoryId,
            name = categoryName ?: EMPTY_STRING,
            color = categoryColor ?: EMPTY_STRING
        )
    } else {
        null
    }
}
