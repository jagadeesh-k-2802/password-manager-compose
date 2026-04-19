package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.PasswordItemEntity
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordItemMapperTest {

    // region PasswordItemEntity → PasswordItemModel

    @Test
    fun `toModel maps all fields correctly`() {
        val entity = PasswordItemEntity(
            id = 1,
            name = "Google",
            username = "user@google.com",
            password = "g00gle!",
            notes = "Primary account",
            createdAt = 1_234_567_890L
        )

        val model = entity.toModel()

        assertEquals(1, model.id)
        assertEquals("Google", model.name)
        assertEquals("user@google.com", model.username)
        assertEquals("g00gle!", model.password)
        assertEquals("Primary account", model.notes)
        assertEquals(1_234_567_890L, model.createdAt)
    }

    @Test
    fun `toModel with empty notes produces model with empty notes`() {
        val entity = PasswordItemEntity(
            id = 2, name = "Apple", username = "user@apple.com",
            password = "apple!", notes = "", createdAt = 100L
        )

        val model = entity.toModel()

        assertEquals("", model.notes)
    }

    // endregion

    // region PasswordItemModel → PasswordItemEntity

    @Test
    fun `toEntity preserves id and createdAt for existing record`() {
        val model = PasswordItemModel(
            id = 10,
            name = "Microsoft",
            username = "user@microsoft.com",
            password = "ms365!",
            notes = "Work email",
            createdAt = 9_999_999L
        )

        val entity = model.toEntity()

        assertEquals(10, entity.id)
        assertEquals("Microsoft", entity.name)
        assertEquals("user@microsoft.com", entity.username)
        assertEquals("ms365!", entity.password)
        assertEquals("Work email", entity.notes)
        assertEquals(9_999_999L, entity.createdAt)
    }

    @Test
    fun `toEntity uses auto-generated defaults when id is null`() {
        val model = PasswordItemModel(
            id = null, name = "New Item", username = "newuser",
            password = "newpass", notes = ""
        )

        val entity = model.toEntity()

        assertEquals(0, entity.id)
        assertEquals("New Item", entity.name)
    }

    @Test
    fun `toEntity uses auto-generated defaults when createdAt is null`() {
        val model = PasswordItemModel(
            id = 5, name = "Item", username = "u",
            password = "p", notes = "", createdAt = null
        )

        val entity = model.toEntity()

        // Falls into else branch (id exists but createdAt is null)
        assertEquals(0, entity.id)
    }

    @Test
    fun `roundtrip entity-model-entity preserves all fields for existing record`() {
        val original = PasswordItemEntity(
            id = 3, name = "Amazon", username = "shopper", password = "prime!",
            notes = "Shopping", createdAt = 42_000L
        )

        val roundtripped = original.toModel().toEntity()

        assertEquals(original.id, roundtripped.id)
        assertEquals(original.name, roundtripped.name)
        assertEquals(original.username, roundtripped.username)
        assertEquals(original.password, roundtripped.password)
        assertEquals(original.notes, roundtripped.notes)
        assertEquals(original.createdAt, roundtripped.createdAt)
    }

    // endregion
}
