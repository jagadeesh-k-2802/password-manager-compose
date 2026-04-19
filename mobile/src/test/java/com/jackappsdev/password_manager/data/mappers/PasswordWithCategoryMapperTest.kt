package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.entity.CategoryEntity
import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity
import com.jackappsdev.password_manager.data.local.entity.PasswordWithCategoryEntity
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordWithCategoryMapperTest {

    private fun makePasswordEntity(
        id: Int = 1,
        categoryId: Int? = 2,
        createdAt: Long = 1_000_000L
    ) = PasswordItemEntity(
        id = id,
        name = "Netflix",
        username = "user@example.com",
        password = "netf1ix!",
        notes = "Shared account",
        website = "https://netflix.com",
        isAddedToWatch = false,
        categoryId = categoryId,
        createdAt = createdAt
    )

    private fun makeCategoryEntity() = CategoryEntity(
        id = 2,
        name = "Entertainment",
        color = "#FF0000",
        createdAt = 500L
    )

    // region PasswordWithCategoryEntity → PasswordWithCategoryModel

    @Test
    fun `toModel maps password item fields correctly`() {
        val entity = PasswordWithCategoryEntity(
            passwordItem = makePasswordEntity(),
            categoryEntity = makeCategoryEntity()
        )

        val model = entity.toModel()

        assertEquals(1, model.id)
        assertEquals("Netflix", model.name)
        assertEquals("user@example.com", model.username)
        assertEquals("netf1ix!", model.password)
        assertEquals("Shared account", model.notes)
        assertEquals("https://netflix.com", model.website)
        assertEquals(false, model.isAddedToWatch)
        assertEquals(1_000_000L, model.createdAt)
    }

    @Test
    fun `toModel maps category fields when category exists`() {
        val entity = PasswordWithCategoryEntity(
            passwordItem = makePasswordEntity(),
            categoryEntity = makeCategoryEntity()
        )

        val model = entity.toModel()

        assertEquals(2, model.categoryId)
        assertEquals("Entertainment", model.categoryName)
        assertEquals("#FF0000", model.categoryColor)
    }

    @Test
    fun `toModel sets category fields to null when category is absent`() {
        val entity = PasswordWithCategoryEntity(
            passwordItem = makePasswordEntity(categoryId = null),
            categoryEntity = null
        )

        val model = entity.toModel()

        assertNull(model.categoryId)
        assertNull(model.categoryName)
        assertNull(model.categoryColor)
    }

    @Test
    fun `toModel maps images list from password entity`() {
        val img = byteArrayOf(10, 20, 30)
        val passwordEntity = makePasswordEntity().copy(images = listOf(img))
        val entity = PasswordWithCategoryEntity(passwordItem = passwordEntity, categoryEntity = null)

        val model = entity.toModel()

        assertEquals(1, model.images.size)
        assertTrue(model.images[0].contentEquals(img))
    }

    // endregion

    // region PasswordWithCategoryModel → PasswordItemEntity

    @Test
    fun `toPasswordItemEntity preserves id and createdAt for existing record`() {
        val model = PasswordWithCategoryModel(
            id = 5,
            name = "Spotify",
            username = "listener",
            password = "melody123",
            notes = "",
            website = "https://spotify.com",
            isAddedToWatch = true,
            categoryId = 3,
            categoryName = "Music",
            categoryColor = "#1DB954",
            createdAt = 9_000_000L
        )

        val entity = model.toPasswordItemEntity()

        assertEquals(5, entity.id)
        assertEquals("Spotify", entity.name)
        assertEquals("listener", entity.username)
        assertEquals("melody123", entity.password)
        assertEquals(true, entity.isAddedToWatch)
        assertEquals(3, entity.categoryId)
        assertEquals(9_000_000L, entity.createdAt)
    }

    @Test
    fun `toPasswordItemEntity uses auto-generated defaults when id is null`() {
        val model = PasswordWithCategoryModel(
            id = null,
            name = "New",
            username = "u",
            password = "p",
            notes = "",
            website = "",
            isAddedToWatch = false
        )

        val entity = model.toPasswordItemEntity()

        assertEquals(0, entity.id)
    }

    @Test
    fun `toPasswordItemEntity uses auto-generated defaults when createdAt is null`() {
        val model = PasswordWithCategoryModel(
            id = 10,
            name = "Item",
            username = "u",
            password = "p",
            notes = "",
            website = "",
            isAddedToWatch = false,
            createdAt = null
        )

        val entity = model.toPasswordItemEntity()

        assertEquals(0, entity.id)
    }

    @Test
    fun `toPasswordItemEntity does not carry category entity fields into password entity`() {
        val model = PasswordWithCategoryModel(
            id = 1,
            name = "App",
            username = "u",
            password = "p",
            notes = "",
            website = "",
            isAddedToWatch = false,
            categoryId = 7,
            categoryName = "ShouldNotAppear",
            categoryColor = "#IGNORE",
            createdAt = 1L
        )

        val entity = model.toPasswordItemEntity()

        // PasswordItemEntity only stores categoryId, not name/color
        assertEquals(7, entity.categoryId)
    }

    // endregion
}
