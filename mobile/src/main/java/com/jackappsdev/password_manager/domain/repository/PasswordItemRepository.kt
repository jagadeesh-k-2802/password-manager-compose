package com.jackappsdev.password_manager.domain.repository

import com.jackappsdev.password_manager.domain.model.PasswordCategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.Flow

interface PasswordItemRepository {
    fun getPasswordItems(
        orderBy: String,
        filterBy: String,
        query: String
    ): Flow<List<PasswordItemModel>>

    suspend fun getUniqueUsernames(username: String, limit: Int): List<String>
    fun getPasswordItem(id: Int): Flow<PasswordCategoryModel?>
    suspend fun insertPasswordItem(item: PasswordItemModel)
    suspend fun removePasswordsFromWatch()
    suspend fun deletePasswordItem(item: PasswordCategoryModel)
}
