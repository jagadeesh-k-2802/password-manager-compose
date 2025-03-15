package com.jackappsdev.password_manager.data.repository

import com.jackappsdev.password_manager.data.local.PasswordDao
import com.jackappsdev.password_manager.data.mappers.toEntity
import com.jackappsdev.password_manager.data.mappers.toModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordItemRepositoryImpl(
    private val passwordDao: PasswordDao
) : PasswordItemRepository {
    override fun getPasswordItems(): Flow<List<PasswordItemModel>> {
        return passwordDao.getAllPasswordEntities().map { items -> items.map { it.toModel() } }
    }

    override fun getPasswordItem(id: Int): Flow<PasswordItemModel?> {
        return passwordDao.getPasswordEntity(id).map { it?.toModel() }
    }

    override suspend fun upsertPasswordItem(item: PasswordItemModel) {
        passwordDao.upsertPasswordEntity(item.toEntity())
    }

    override suspend fun deletePasswordItem(item: PasswordItemModel) {
        passwordDao.deletePasswordEntity(item.toEntity())
    }

    override suspend fun deleteAllPasswords() {
        passwordDao.deleteAllPasswords()
    }
}
