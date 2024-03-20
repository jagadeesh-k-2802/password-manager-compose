package com.jagadeesh.passwordmanager.data.repository

import com.jagadeesh.passwordmanager.data.local.CategoryDao
import com.jagadeesh.passwordmanager.data.mappers.toEntity
import com.jagadeesh.passwordmanager.data.mappers.toModel
import com.jagadeesh.passwordmanager.domain.model.CategoryModel
import com.jagadeesh.passwordmanager.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<CategoryModel>> {
        return categoryDao.getAllCategories().map { items ->
            items.map { it.toModel() }
        }
    }

    override fun getCategoryItem(id: Int): Flow<CategoryModel?> {
        return categoryDao.getCategory(id).map { it?.toModel() }
    }

    override suspend fun insertCategoryItem(item: CategoryModel) {
        categoryDao.insertCategory(item.toEntity())
    }

    override suspend fun deleteCategoryItem(item: CategoryModel) {
        categoryDao.deleteCategory(item.toEntity())
    }
}
