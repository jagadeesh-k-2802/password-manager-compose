package com.jackappsdev.password_manager.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SortByTest {

    @Test
    fun `ALPHABET_ASCENDING orderBy returns name ASC`() {
        assertEquals("name ASC", SortBy.ALPHABET_ASCENDING.orderBy())
    }

    @Test
    fun `ALPHABET_DESCENDING orderBy returns name DESC`() {
        assertEquals("name DESC", SortBy.ALPHABET_DESCENDING.orderBy())
    }

    @Test
    fun `NEWEST orderBy returns created_at DESC`() {
        assertEquals("created_at DESC", SortBy.NEWEST.orderBy())
    }

    @Test
    fun `OLDEST orderBy returns created_at ASC`() {
        assertEquals("created_at ASC", SortBy.OLDEST.orderBy())
    }

    @Test
    fun `all SortBy values produce non-blank ORDER BY clauses`() {
        SortBy.entries.forEach { sortBy ->
            assertTrue(
                "orderBy() for $sortBy must not be blank",
                sortBy.orderBy().isNotBlank()
            )
        }
    }

    @Test
    fun `all SortBy values produce distinct ORDER BY clauses`() {
        val clauses = SortBy.entries.map { it.orderBy() }
        assertEquals(
            "All orderBy clauses must be distinct",
            clauses.size,
            clauses.toSet().size
        )
    }

    @Test
    fun `ascending and descending sorts for same column differ only in direction keyword`() {
        val ascending = SortBy.ALPHABET_ASCENDING.orderBy()
        val descending = SortBy.ALPHABET_DESCENDING.orderBy()

        assertTrue(ascending.contains("name"))
        assertTrue(descending.contains("name"))
        assertTrue(ascending.endsWith("ASC"))
        assertTrue(descending.endsWith("DESC"))
    }

    @Test
    fun `newest and oldest sorts target the created_at column`() {
        assertTrue(SortBy.NEWEST.orderBy().startsWith("created_at"))
        assertTrue(SortBy.OLDEST.orderBy().startsWith("created_at"))
    }
}
