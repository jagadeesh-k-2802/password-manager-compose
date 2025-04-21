package com.jackappsdev.password_manager.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.jackappsdev.password_manager.data.local.dao.PasswordDao
import com.jackappsdev.password_manager.data.mappers.toEntity
import com.jackappsdev.password_manager.data.mappers.toModel
import com.jackappsdev.password_manager.data.mappers.toPasswordItemEntity
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordItemRepositoryImpl(
    private val passwordDao: PasswordDao
) : PasswordItemRepository {

    override fun getPasswordItems(orderBy: String, filterBy: String, query: String): Flow<List<PasswordItemModel>> {
        val likeParam = "name LIKE '%$query%' OR username LIKE '%$query%'"

        val sql = if (filterBy.isEmpty()) {
            SimpleSQLiteQuery(
                "SELECT * FROM password_items WHERE $likeParam ORDER BY $orderBy"
            )
        } else {
            SimpleSQLiteQuery(
                "SELECT * FROM password_items WHERE ($likeParam) AND $filterBy ORDER BY $orderBy"
            )
        }

        return passwordDao.getAllPasswordEntities(sql).map { items -> items.map { it.toModel() } }
    }

    override suspend fun getUniqueUsernames(username: String, limit: Int): List<String> {
        return passwordDao.getUniqueUsernames("%$username%", limit)
    }

    override fun getPasswordItem(id: Int): Flow<PasswordWithCategoryModel?> {
        return passwordDao.getPasswordItem(id).map { it?.toModel() }
    }

    override suspend fun upsertPasswordItem(item: PasswordItemModel) {
        passwordDao.upsertPasswordEntity(item.toEntity())
    }

    override suspend fun removePasswordsFromWatch() {
        passwordDao.removePasswordsFromWatch()
    }

    override suspend fun deletePasswordItem(item: PasswordWithCategoryModel) {
        passwordDao.deletePasswordEntity(item.toPasswordItemEntity())
    }
}
