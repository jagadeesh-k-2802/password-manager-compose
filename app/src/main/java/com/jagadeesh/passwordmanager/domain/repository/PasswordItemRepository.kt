package com.jagadeesh.passwordmanager.domain.repository

import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.presentation.screens.home.SortBy
import kotlinx.coroutines.flow.Flow

interface PasswordItemRepository {
    fun getPasswordItems(orderBy: String): Flow<List<PasswordItemModel>>
    suspend fun insertPasswordItem(item: PasswordItemModel)
    suspend fun deletePasswordItem(item: PasswordItemModel)
}
