package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordItemMapperTest {

    // region PasswordItemEntity → PasswordItemModel

    @Test
    fun `toModel maps all scalar fields correctly`() {
        val entity = PasswordItemEntity(
            id = 1,
            name = "GitHub",
            username = "dev@example.com",
            password = "s3cr3t!",
            notes = "Work account",
            website = "https://github.com",
            isAddedToWatch = true,
            categoryId = 3,
            createdAt = 1_700_000_000L
        )

        val model = entity.toModel()

        assertEquals(entity.id, model.id)
        assertEquals(entity.name, model.name)
        assertEquals(entity.username, model.username)
        assertEquals(entity.password, model.password)
        assertEquals(entity.notes, model.notes)
        assertEquals(entity.website, model.website)
        assertEquals(entity.isAddedToWatch, model.isAddedToWatch)
        assertEquals(entity.categoryId, model.categoryId)
        assertEquals(entity.createdAt, model.createdAt)
    }

    @Test
    fun `toModel preserves null categoryId`() {
        val entity = PasswordItemEntity(
            id = 2,
            name = "Twitter",
            username = "user",
            password = "pass",
            notes = "",
            website = "",
            isAddedToWatch = false,
            categoryId = null,
            createdAt = 1_000L
        )

        val model = entity.toModel()

        assertNull(model.categoryId)
    }

    @Test
    fun `toModel maps images list correctly`() {
        val img1 = byteArrayOf(1, 2, 3)
        val img2 = byteArrayOf(4, 5, 6)
        val entity = PasswordItemEntity(
            id = 3,
            name = "Drive",
            username = "a",
            password = "b",
            notes = "",
            website = "",
            isAddedToWatch = false,
            images = listOf(img1, img2),
            createdAt = 100L
        )

        val model = entity.toModel()

        assertEquals(2, model.images.size)
        assertTrue(model.images[0].contentEquals(img1))
        assertTrue(model.images[1].contentEquals(img2))
    }

    @Test
    fun `toModel with empty images produces empty list`() {
        val entity = PasswordItemEntity(
            id = 4, name = "N", username = "u", password = "p",
            notes = "", website = "", isAddedToWatch = false, images = emptyList(), createdAt = 1L
        )

        val model = entity.toModel()

        assertTrue(model.images.isEmpty())
    }

    // endregion

    // region PasswordItemModel → PasswordItemEntity

    @Test
    fun `toEntity preserves id and createdAt for existing record`() {
        val model = PasswordItemModel(
            id = 10,
            name = "Gmail",
            username = "user@gmail.com",
            password = "hunter2",
            notes = "Primary email",
            website = "https://mail.google.com",
            isAddedToWatch = false,
            categoryId = 2,
            createdAt = 1_600_000_000L
        )

        val entity = model.toEntity()

        assertEquals(10, entity.id)
        assertEquals(1_600_000_000L, entity.createdAt)
        assertEquals("Gmail", entity.name)
        assertEquals("user@gmail.com", entity.username)
        assertEquals("hunter2", entity.password)
        assertEquals(2, entity.categoryId)
    }

    @Test
    fun `toEntity uses auto-generated defaults when id is null`() {
        val model = PasswordItemModel(
            id = null,
            name = "New Item",
            username = "newuser",
            password = "newpass",
            notes = "",
            website = "",
            isAddedToWatch = false
        )

        val entity = model.toEntity()

        // Room will auto-generate when id == 0 (default)
        assertEquals(0, entity.id)
        assertEquals("New Item", entity.name)
    }

    @Test
    fun `toEntity uses auto-generated defaults when createdAt is null`() {
        val model = PasswordItemModel(
            id = 99,
            name = "Item",
            username = "u",
            password = "p",
            notes = "",
            website = "",
            isAddedToWatch = false,
            createdAt = null
        )

        val entity = model.toEntity()

        // Falls into the else branch → id reset to auto-generate default
        assertEquals(0, entity.id)
    }

    @Test
    fun `toEntity maps isAddedToWatch flag correctly`() {
        val model = PasswordItemModel(
            id = 5, name = "Watch item", username = "u", password = "p",
            notes = "", website = "", isAddedToWatch = true, createdAt = 1L
        )

        val entity = model.toEntity()

        assertTrue(entity.isAddedToWatch)
    }

    @Test
    fun `toEntity preserves null categoryId`() {
        val model = PasswordItemModel(
            id = 6, name = "No Cat", username = "u", password = "p",
            notes = "", website = "", isAddedToWatch = false, categoryId = null, createdAt = 1L
        )

        val entity = model.toEntity()

        assertNull(entity.categoryId)
    }

    // endregion
}
