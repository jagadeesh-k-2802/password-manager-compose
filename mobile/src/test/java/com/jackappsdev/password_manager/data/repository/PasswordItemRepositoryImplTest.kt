package com.jackappsdev.password_manager.data.repository

import com.jackappsdev.password_manager.data.local.dao.PasswordDao
import com.jackappsdev.password_manager.data.local.entity.CategoryEntity
import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity
import com.jackappsdev.password_manager.data.local.entity.PasswordWithCategoryEntity
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordItemRepositoryImplTest {

    private lateinit var passwordDao: PasswordDao
    private lateinit var repository: PasswordItemRepositoryImpl

    @Before
    fun setUp() {
        passwordDao = mockk()
        repository = PasswordItemRepositoryImpl(passwordDao)
    }

    // region getPasswordItems — SQL query construction

    @Suppress("UnusedFlow")
    @Test
    fun `getPasswordItems without filterBy generates query without AND clause`() = runTest {
        every { passwordDao.getAllPasswordEntities(any()) } returns flowOf(emptyList())

        repository.getPasswordItems("name ASC", "", "test").first()

        coVerify {
            passwordDao.getAllPasswordEntities(match { query ->
                !query.sql.contains(" AND ") &&
                query.sql.contains("WHERE") &&
                query.sql.contains("name LIKE '%test%'")
            })
        }
    }

    @Suppress("UnusedFlow")
    @Test
    fun `getPasswordItems with filterBy includes AND and filter clause`() = runTest {
        every { passwordDao.getAllPasswordEntities(any()) } returns flowOf(emptyList())

        repository.getPasswordItems("name ASC", "category_id = 3", "email").first()

        coVerify {
            passwordDao.getAllPasswordEntities(match { query ->
                query.sql.contains("AND") &&
                query.sql.contains("category_id = 3") &&
                query.sql.contains("name LIKE '%email%'")
            })
        }
    }

    @Suppress("UnusedFlow")
    @Test
    fun `getPasswordItems embeds search term in both name and username LIKE clauses`() = runTest {
        every { passwordDao.getAllPasswordEntities(any()) } returns flowOf(emptyList())

        repository.getPasswordItems("name ASC", "", "alice").first()

        coVerify {
            passwordDao.getAllPasswordEntities(match { query ->
                query.sql.contains("name LIKE '%alice%'") &&
                query.sql.contains("username LIKE '%alice%'")
            })
        }
    }

    @Suppress("UnusedFlow")
    @Test
    fun `getPasswordItems places ORDER BY clause using provided orderBy param`() = runTest {
        every { passwordDao.getAllPasswordEntities(any()) } returns flowOf(emptyList())

        repository.getPasswordItems("created_at DESC", "", "").first()

        coVerify {
            passwordDao.getAllPasswordEntities(match { query ->
                query.sql.contains("ORDER BY created_at DESC")
            })
        }
    }

    @Test
    fun `getPasswordItems maps returned entities to domain models`() = runTest {
        val entities = listOf(
            PasswordItemEntity(
                id = 1, name = "GitHub", username = "dev", password = "gh-pass",
                notes = "", website = "", isAddedToWatch = false, createdAt = 100L
            )
        )
        every { passwordDao.getAllPasswordEntities(any()) } returns flowOf(entities)

        val results = repository.getPasswordItems("name ASC", "", "").first()

        assertEquals(1, results.size)
        assertEquals("GitHub", results[0].name)
        assertEquals("dev", results[0].username)
    }

    @Test
    fun `getPasswordItems emits empty list when dao returns no rows`() = runTest {
        every { passwordDao.getAllPasswordEntities(any()) } returns flowOf(emptyList())

        val results = repository.getPasswordItems("name ASC", "", "noresult").first()

        assertTrue(results.isEmpty())
    }

    // endregion

    // region getUniqueUsernames

    @Test
    fun `getUniqueUsernames wraps search term with percent wildcards`() = runTest {
        coEvery { passwordDao.getUniqueUsernames("%john%", 10) } returns listOf("john@example.com")

        val result = repository.getUniqueUsernames("john", 10)

        assertEquals(listOf("john@example.com"), result)
    }

    @Test
    fun `getUniqueUsernames returns empty list when no matches found`() = runTest {
        coEvery { passwordDao.getUniqueUsernames(any(), any()) } returns emptyList()

        val result = repository.getUniqueUsernames("zzz", 5)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getUniqueUsernames respects the limit parameter`() = runTest {
        val expected = listOf("a@b.com", "c@d.com", "e@f.com")
        coEvery { passwordDao.getUniqueUsernames("%a%", 3) } returns expected

        val result = repository.getUniqueUsernames("a", 3)

        assertEquals(3, result.size)
    }

    // endregion

    // region getPasswordItem

    @Test
    fun `getPasswordItem returns mapped model when entity exists`() = runTest {
        val passwordEntity = PasswordItemEntity(
            id = 5, name = "Slack", username = "team@slack.com", password = "slack-pass",
            notes = "", website = "https://slack.com", isAddedToWatch = false, createdAt = 500L
        )
        val categoryEntity = CategoryEntity(id = 2, name = "Work", color = "#0000FF", createdAt = 100L)
        val withCategoryEntity = PasswordWithCategoryEntity(passwordEntity, categoryEntity)
        every { passwordDao.getPasswordItem(5) } returns flowOf(withCategoryEntity)

        val result = repository.getPasswordItem(5).first()

        assertEquals(5, result?.id)
        assertEquals("Slack", result?.name)
        assertEquals("Work", result?.categoryName)
        assertEquals("#0000FF", result?.categoryColor)
    }

    @Test
    fun `getPasswordItem returns null when entity is absent`() = runTest {
        every { passwordDao.getPasswordItem(999) } returns flowOf(null)

        val result = repository.getPasswordItem(999).first()

        assertNull(result)
    }

    @Test
    fun `getPasswordItem maps null category correctly`() = runTest {
        val passwordEntity = PasswordItemEntity(
            id = 6, name = "Solo", username = "u", password = "p",
            notes = "", website = "", isAddedToWatch = false, categoryId = null, createdAt = 1L
        )
        every { passwordDao.getPasswordItem(6) } returns flowOf(
            PasswordWithCategoryEntity(passwordEntity, null)
        )

        val result = repository.getPasswordItem(6).first()

        assertNull(result?.categoryId)
        assertNull(result?.categoryName)
    }

    // endregion

    // region upsertPasswordItem

    @Test
    fun `upsertPasswordItem delegates to dao with correct entity`() = runTest {
        val model = PasswordItemModel(
            id = 10, name = "Notion", username = "writer@notion.so", password = "notion!",
            notes = "Notes app", website = "https://notion.so",
            isAddedToWatch = false, categoryId = 1, createdAt = 200L
        )
        coEvery { passwordDao.upsertPasswordEntity(any()) } returns longArrayOf(10L)

        repository.upsertPasswordItem(model)

        coVerify {
            passwordDao.upsertPasswordEntity(match { entity ->
                entity.id == 10 && entity.name == "Notion" && entity.username == "writer@notion.so"
            })
        }
    }

    // endregion

    // region deletePasswordItem

    @Test
    fun `deletePasswordItem delegates to dao with correct entity`() = runTest {
        val model = PasswordWithCategoryModel(
            id = 3, name = "Figma", username = "designer", password = "fig123",
            notes = "", website = "", isAddedToWatch = false, createdAt = 300L
        )
        coEvery { passwordDao.deletePasswordEntity(any()) } returns Unit

        repository.deletePasswordItem(model)

        coVerify {
            passwordDao.deletePasswordEntity(match { entity ->
                entity.id == 3 && entity.name == "Figma"
            })
        }
    }

    // endregion

    // region removePasswordsFromWatch

    @Test
    fun `removePasswordsFromWatch delegates to dao`() = runTest {
        coEvery { passwordDao.removePasswordsFromWatch() } returns Unit

        repository.removePasswordsFromWatch()

        coVerify { passwordDao.removePasswordsFromWatch() }
    }

    // endregion
}
