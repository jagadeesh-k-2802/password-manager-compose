package com.jagadeesh.passwordmanager.domain.repository

import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.Flow

interface PasswordItemRepository {
    fun getPasswordItems(
        orderBy: String,
        filterBy: String,
        query: String
    ): Flow<List<PasswordItemModel>>

    fun getPasswordItem(id: Int): Flow<PasswordItemModel?>
    suspend fun insertPasswordItem(item: PasswordItemModel)
    suspend fun deletePasswordItem(item: PasswordItemModel)
}
