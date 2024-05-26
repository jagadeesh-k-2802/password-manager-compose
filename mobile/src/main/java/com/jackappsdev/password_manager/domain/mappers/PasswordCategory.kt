package com.jackappsdev.password_manager.domain.mappers

import com.jackappsdev.password_manager.domain.model.PasswordCategoryModel
import com.jackappsdev.password_manager.shared.data.dto.PasswordItemDto

fun PasswordCategoryModel.toPasswordItemDto(): PasswordItemDto {
    return PasswordItemDto(
        id = id ?: 0,
        name = name,
        username = username,
        password = password,
        notes = notes,
        createdAt = createdAt ?: 0
    )
}
