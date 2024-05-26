package com.jackappsdev.password_manager.domain.mappers

import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.shared.data.dto.PasswordItemDto

fun PasswordItemDto.toPasswordItemModel(): PasswordItemModel {
    return PasswordItemModel(
        id = id,
        name = name,
        username = username,
        password = password,
        notes = notes,
        createdAt = createdAt
    )
}
