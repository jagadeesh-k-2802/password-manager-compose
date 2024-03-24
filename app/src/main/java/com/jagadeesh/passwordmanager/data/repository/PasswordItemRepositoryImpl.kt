package com.jagadeesh.passwordmanager.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.jagadeesh.passwordmanager.data.local.PasswordDao
import com.jagadeesh.passwordmanager.data.mappers.toEntity
import com.jagadeesh.passwordmanager.data.mappers.toModel
import com.jagadeesh.passwordmanager.data.mappers.toPasswordItemEntity
import com.jagadeesh.passwordmanager.domain.model.PasswordCategoryModel
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordItemRepositoryImpl(
    private val passwordDao: PasswordDao
) : PasswordItemRepository {
    override fun getPasswordItems(
        orderBy: String,
        filterBy: String,
        query: String
    ): Flow<List<PasswordItemModel>> {
        val sql = if (filterBy.isEmpty()) {
            SimpleSQLiteQuery(
                "SELECT * FROM password_items WHERE name LIKE '%$query%' ORDER BY $orderBy"
            )
        } else {
            SimpleSQLiteQuery(
                "SELECT * FROM password_items WHERE name LIKE '%$query%' AND $filterBy ORDER BY $orderBy"
            )
        }

        return passwordDao.getAllPasswordEntities(sql).map { items ->
            items.map { it.toModel() }
        }
    }

    override fun getPasswordItem(id: Int): Flow<PasswordCategoryModel?> {
        return passwordDao.getPasswordEntity(id).map { it?.toModel() }
    }

    override suspend fun insertPasswordItem(item: PasswordItemModel) {
        passwordDao.insertPasswordEntity(item.toEntity())
    }

    override suspend fun deletePasswordItem(item: PasswordCategoryModel) {
        passwordDao.deletePasswordEntity(item.toPasswordItemEntity())
    }
}
