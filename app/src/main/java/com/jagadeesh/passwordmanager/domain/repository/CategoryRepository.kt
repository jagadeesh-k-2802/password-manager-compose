package com.jagadeesh.passwordmanager.domain.repository

import com.jagadeesh.passwordmanager.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryModel>>
    fun getCategoryItem(id: Int): Flow<CategoryModel?>
    suspend fun insertCategoryItem(item: CategoryModel)
    suspend fun deleteCategoryItem(item: CategoryModel)
}
