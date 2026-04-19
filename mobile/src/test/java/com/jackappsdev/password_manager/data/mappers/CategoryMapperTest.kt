package com.jackappsdev.password_manager.data.mappers

import com.jackappsdev.password_manager.data.local.entity.CategoryEntity
import com.jackappsdev.password_manager.domain.model.CategoryModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CategoryMapperTest {

    // region CategoryEntity → CategoryModel

    @Test
    fun `toModel maps all fields from entity correctly`() {
        val entity = CategoryEntity(
            id = 1,
            name = "Work",
            color = "#FF5733",
            createdAt = 1_700_000_000L
        )

        val model = entity.toModel()

        assertEquals(1, model.id)
        assertEquals("Work", model.name)
        assertEquals("#FF5733", model.color)
        assertEquals(1_700_000_000L, model.createdAt)
    }

    @Test
    fun `toModel with empty name and color produces model with empty strings`() {
        val entity = CategoryEntity(id = 2, name = "", color = "", createdAt = 500L)

        val model = entity.toModel()

        assertEquals("", model.name)
        assertEquals("", model.color)
    }

    // endregion

    // region CategoryModel → CategoryEntity

    @Test
    fun `toEntity preserves id and createdAt for existing category`() {
        val model = CategoryModel(
            id = 10,
            name = "Personal",
            color = "#00AABB",
            createdAt = 1_234_567_890L
        )

        val entity = model.toEntity()

        assertEquals(10, entity.id)
        assertEquals("Personal", entity.name)
        assertEquals("#00AABB", entity.color)
        assertEquals(1_234_567_890L, entity.createdAt)
    }

    @Test
    fun `toEntity uses auto-generated defaults when id is null`() {
        val model = CategoryModel(id = null, name = "Finance", color = "#33FF57")

        val entity = model.toEntity()

        assertEquals(0, entity.id)  // Room default auto-generate sentinel
        assertEquals("Finance", entity.name)
        assertEquals("#33FF57", entity.color)
    }

    @Test
    fun `toEntity uses auto-generated defaults when createdAt is null even if id exists`() {
        val model = CategoryModel(id = 5, name = "Travel", color = "#1234AB", createdAt = null)

        val entity = model.toEntity()

        // Falls into else branch because createdAt is null
        assertEquals(0, entity.id)
    }

    @Test
    fun `toEntity assigns a non-zero createdAt when using auto-generated path`() {
        val before = System.currentTimeMillis()
        val model = CategoryModel(name = "New Category", color = "#FFFFFF")

        val entity = model.toEntity()

        assertEquals(0, entity.id)
        // The entity gets a fresh timestamp from System.currentTimeMillis() in its default
        assert(entity.createdAt >= before)
    }

    @Test
    fun `roundtrip entity-model-entity preserves all fields`() {
        val original = CategoryEntity(id = 7, name = "Crypto", color = "#ABCDEF", createdAt = 999L)

        val roundtripped = original.toModel().toEntity()

        assertEquals(original.id, roundtripped.id)
        assertEquals(original.name, roundtripped.name)
        assertEquals(original.color, roundtripped.color)
        assertEquals(original.createdAt, roundtripped.createdAt)
    }

    @Test
    fun `two categories with different colors are not equal after mapping`() {
        val model1 = CategoryModel(id = 1, name = "Same", color = "#RED", createdAt = 1L)
        val model2 = CategoryModel(id = 1, name = "Same", color = "#BLUE", createdAt = 1L)

        assertNotEquals(model1.toEntity(), model2.toEntity())
    }

    // endregion
}
