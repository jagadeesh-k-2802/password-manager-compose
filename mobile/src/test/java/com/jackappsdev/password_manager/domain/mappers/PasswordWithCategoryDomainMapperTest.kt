package com.jackappsdev.password_manager.domain.mappers

import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PasswordWithCategoryDomainMapperTest {

    private fun makeModel(
        id: Int? = 1,
        categoryId: Int? = 5,
        categoryName: String? = "Work",
        categoryColor: String? = "#0000FF",
        createdAt: Long? = 1_234_567L
    ) = PasswordWithCategoryModel(
        id = id,
        name = "Jira",
        username = "dev@company.com",
        password = "agile123",
        notes = "Project tracker",
        website = "https://jira.atlassian.com",
        isAddedToWatch = false,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryColor = categoryColor,
        createdAt = createdAt
    )

    // region toPasswordItemDto

    @Test
    fun `toPasswordItemDto maps id correctly`() {
        val dto = makeModel(id = 42).toPasswordItemDto()
        assertEquals(42, dto.id)
    }

    @Test
    fun `toPasswordItemDto uses zero when id is null`() {
        val dto = makeModel(id = null).toPasswordItemDto()
        assertEquals(0, dto.id)
    }

    @Test
    fun `toPasswordItemDto maps name, username, password, notes`() {
        val model = makeModel()
        val dto = model.toPasswordItemDto()

        assertEquals("Jira", dto.name)
        assertEquals("dev@company.com", dto.username)
        assertEquals("agile123", dto.password)
        assertEquals("Project tracker", dto.notes)
    }

    @Test
    fun `toPasswordItemDto maps createdAt correctly`() {
        val dto = makeModel(createdAt = 9_876_543L).toPasswordItemDto()
        assertEquals(9_876_543L, dto.createdAt)
    }

    @Test
    fun `toPasswordItemDto uses zero when createdAt is null`() {
        val dto = makeModel(createdAt = null).toPasswordItemDto()
        assertEquals(0L, dto.createdAt)
    }

    // endregion

    // region toCategoryModel

    @Test
    fun `toCategoryModel returns CategoryModel when categoryId is set`() {
        val model = makeModel(categoryId = 5, categoryName = "Work", categoryColor = "#0000FF")

        val category = model.toCategoryModel()

        assertNotNull(category)
        assertEquals(5, category!!.id)
        assertEquals("Work", category.name)
        assertEquals("#0000FF", category.color)
    }

    @Test
    fun `toCategoryModel returns null when categoryId is null`() {
        val model = makeModel(categoryId = null)

        val category = model.toCategoryModel()

        assertNull(category)
    }

    @Test
    fun `toCategoryModel falls back to empty string when categoryName is null`() {
        val model = makeModel(categoryId = 3, categoryName = null, categoryColor = "#AABBCC")

        val category = model.toCategoryModel()

        assertNotNull(category)
        assertEquals(EMPTY_STRING, category!!.name)
    }

    @Test
    fun `toCategoryModel falls back to empty string when categoryColor is null`() {
        val model = makeModel(categoryId = 3, categoryName = "Misc", categoryColor = null)

        val category = model.toCategoryModel()

        assertNotNull(category)
        assertEquals(EMPTY_STRING, category!!.color)
    }

    @Test
    fun `toCategoryModel does not carry createdAt into category`() {
        val model = makeModel(categoryId = 1, categoryName = "Cat", categoryColor = "#FFF")

        val category = model.toCategoryModel()

        // CategoryModel returned from domain mapper has no createdAt (it's null by default)
        assertNull(category!!.createdAt)
    }

    // endregion
}
