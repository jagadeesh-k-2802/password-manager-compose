package com.jagadeesh.passwordmanager.data.repository

import com.jagadeesh.passwordmanager.data.local.PasswordDatabase
import com.jagadeesh.passwordmanager.data.mappers.toEntity
import com.jagadeesh.passwordmanager.data.mappers.toModel
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordItemRepositoryImpl(
    private val passwordDatabase: PasswordDatabase
) : PasswordItemRepository {
    override fun getPasswordItems(orderBy: String): Flow<List<PasswordItemModel>> {
        return passwordDatabase.passwordDao().getPasswordEntities(orderBy).map { items ->
            items.map { it.toModel() }
        }
    }

    override suspend fun insertPasswordItem(item: PasswordItemModel) {
        passwordDatabase.passwordDao().insertPasswordEntity(item.toEntity())
    }

    override suspend  fun deletePasswordItem(item: PasswordItemModel) {
        passwordDatabase.passwordDao().deletePasswordEntity(item.toEntity())
    }
}
