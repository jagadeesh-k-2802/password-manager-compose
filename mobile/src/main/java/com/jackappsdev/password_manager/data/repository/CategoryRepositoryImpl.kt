package com.jackappsdev.password_manager.data.repository

import com.jackappsdev.password_manager.data.local.dao.CategoryDao
import com.jackappsdev.password_manager.data.mappers.toEntity
import com.jackappsdev.password_manager.data.mappers.toModel
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
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

    override suspend fun insertCategoryItem(item: CategoryModel): Long {
        return categoryDao.insertCategory(item.toEntity())
    }

    override suspend fun deleteCategoryItem(item: CategoryModel) {
        categoryDao.deleteCategory(item.toEntity())
    }
}
