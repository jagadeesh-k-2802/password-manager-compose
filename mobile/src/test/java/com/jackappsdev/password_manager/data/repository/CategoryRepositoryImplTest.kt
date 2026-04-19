package com.jackappsdev.password_manager.data.repository

import com.jackappsdev.password_manager.data.local.dao.CategoryDao
import com.jackappsdev.password_manager.data.local.dao.PasswordDao
import com.jackappsdev.password_manager.data.local.entity.CategoryEntity
import com.jackappsdev.password_manager.domain.model.CategoryModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.coVerifyOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {

    private lateinit var passwordDao: PasswordDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        passwordDao = mockk(relaxed = true)
        categoryDao = mockk(relaxed = true)
        repository = CategoryRepositoryImpl(passwordDao, categoryDao)
    }

    // region deleteCategoryItem

    @Test
    fun `deleteCategoryItem removes category from passwords before deleting category`() = runTest {
        val category = CategoryModel(id = 1, name = "Work", color = "#FF0000")

        repository.deleteCategoryItem(category)

        coVerifyOrder {
            passwordDao.removeCategoryFromPasswords(1)
            categoryDao.deleteCategory(any())
        }
    }

    @Test
    fun `deleteCategoryItem skips removeCategoryFromPasswords when id is null`() = runTest {
        val category = CategoryModel(id = null, name = "Orphan", color = "#000")

        repository.deleteCategoryItem(category)

        coVerify(exactly = 0) { passwordDao.removeCategoryFromPasswords(any()) }
        coVerify { categoryDao.deleteCategory(any()) }
    }

    @Test
    fun `deleteCategoryItem passes the correct entity to dao`() = runTest {
        val category = CategoryModel(id = 3, name = "Finance", color = "#00FF00", createdAt = 100L)

        repository.deleteCategoryItem(category)

        coVerify {
            categoryDao.deleteCategory(match { entity ->
                entity.id == 3 && entity.name == "Finance" && entity.color == "#00FF00"
            })
        }
    }

    // endregion

    // region insertCategoryItem

    @Test
    fun `insertCategoryItem delegates to categoryDao and returns generated id`() = runTest {
        coEvery { categoryDao.insertCategory(any()) } returns 42L

        val result = repository.insertCategoryItem(CategoryModel(name = "Travel", color = "#1234AB"))

        assertEquals(42L, result)
    }

    @Test
    fun `insertCategoryItem passes correct name and color to dao`() = runTest {
        coEvery { categoryDao.insertCategory(any()) } returns 1L

        repository.insertCategoryItem(CategoryModel(name = "Health", color = "#FF00FF"))

        coVerify {
            categoryDao.insertCategory(match { it.name == "Health" && it.color == "#FF00FF" })
        }
    }

    @Test
    fun `insertCategoryItem with existing id preserves that id in the entity`() = runTest {
        coEvery { categoryDao.insertCategory(any()) } returns 5L

        repository.insertCategoryItem(CategoryModel(id = 5, name = "Old Cat", color = "#ABC", createdAt = 100L))

        coVerify { categoryDao.insertCategory(match { it.id == 5 }) }
    }

    // endregion

    // region getAllCategories

    @Test
    fun `getAllCategories maps entities to domain models`() = runTest {
        val entities = listOf(
            CategoryEntity(id = 1, name = "Work", color = "#FF0000", createdAt = 100L),
            CategoryEntity(id = 2, name = "Home", color = "#00FF00", createdAt = 200L)
        )
        every { categoryDao.getAllCategories() } returns flowOf(entities)

        val result = repository.getAllCategories().first()

        assertEquals(2, result.size)
        assertEquals("Work", result[0].name)
        assertEquals("Home", result[1].name)
        assertEquals("#FF0000", result[0].color)
    }

    @Test
    fun `getAllCategories emits empty list when no categories exist`() = runTest {
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())

        val result = repository.getAllCategories().first()

        assertEquals(emptyList<CategoryModel>(), result)
    }

    // endregion

    // region getCategoryItem

    @Test
    fun `getCategoryItem returns mapped model when entity exists`() = runTest {
        val entity = CategoryEntity(id = 7, name = "Crypto", color = "#FFCC00", createdAt = 999L)
        every { categoryDao.getCategory(7) } returns flowOf(entity)

        val result = repository.getCategoryItem(7).first()

        assertEquals(7, result?.id)
        assertEquals("Crypto", result?.name)
        assertEquals("#FFCC00", result?.color)
    }

    @Test
    fun `getCategoryItem returns null when entity is absent`() = runTest {
        every { categoryDao.getCategory(99) } returns flowOf(null)

        val result = repository.getCategoryItem(99).first()

        assertNull(result)
    }

    // endregion
}
