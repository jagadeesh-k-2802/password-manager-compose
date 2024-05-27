package com.jackappsdev.password_manager.domain.repository

import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.Flow

interface PasswordItemRepository {
    fun getPasswordItems(): Flow<List<PasswordItemModel>>
    fun getPasswordItem(id: Int): Flow<PasswordItemModel?>
    suspend fun insertPasswordItem(item: PasswordItemModel)
    suspend fun deletePasswordItem(item: PasswordItemModel)
    suspend fun deleteAllPasswords()
}
