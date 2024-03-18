package com.jagadeesh.passwordmanager.data.mappers

import com.jagadeesh.passwordmanager.data.local.PasswordItemEntity
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel

fun PasswordItemEntity.toModel(): PasswordItemModel {
    return PasswordItemModel(
        id = id,
        name = name,
        username = username,
        password = password
    )
}

fun PasswordItemModel.toEntity(): PasswordItemEntity {
    return PasswordItemEntity(
        name = name,
        username = username,
        password = password
    )
}
