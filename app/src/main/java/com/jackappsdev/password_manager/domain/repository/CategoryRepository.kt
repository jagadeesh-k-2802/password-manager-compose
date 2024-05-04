package com.jackappsdev.password_manager.domain.repository

import com.jackappsdev.password_manager.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryModel>>
    fun getCategoryItem(id: Int): Flow<CategoryModel?>
    suspend fun insertCategoryItem(item: CategoryModel): Long
    suspend fun deleteCategoryItem(item: CategoryModel)
}
