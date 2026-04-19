package com.jackappsdev.password_manager.domain.model

import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterByTest {

    @Test
    fun `FilterBy All where returns empty string`() {
        assertEquals(EMPTY_STRING, FilterBy.All.where())
    }

    @Test
    fun `FilterBy Category where returns categoryId equality clause`() {
        val filter = FilterBy.Category(categoryId = 7)
        assertEquals("category_id = 7", filter.where())
    }

    @Test
    fun `FilterBy Category where embeds the exact id`() {
        val filter = FilterBy.Category(categoryId = 999)
        assertTrue(filter.where().contains("999"))
        assertTrue(filter.where().contains("category_id"))
    }

    @Test
    fun `FilterBy NoCategoryItems where returns IS NULL clause`() {
        assertEquals("category_id IS NULL", FilterBy.NoCategoryItems.where())
    }

    @Test
    fun `FilterBy All where produces no SQL that could accidentally filter rows`() {
        val clause = FilterBy.All.where()
        assertTrue("All filter must be empty so no WHERE is appended", clause.isEmpty())
    }

    @Test
    fun `FilterBy Category with id zero produces valid clause`() {
        val clause = FilterBy.Category(categoryId = 0).where()
        assertEquals("category_id = 0", clause)
    }

    @Test
    fun `distinct FilterBy variants produce distinct WHERE clauses`() {
        val allClause = FilterBy.All.where()
        val categoryClause = FilterBy.Category(1).where()
        val noCategory = FilterBy.NoCategoryItems.where()

        assert(allClause != categoryClause)
        assert(allClause != noCategory)
        assert(categoryClause != noCategory)
    }
}
