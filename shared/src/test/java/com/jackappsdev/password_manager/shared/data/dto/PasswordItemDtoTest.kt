package com.jackappsdev.password_manager.shared.data.dto

import com.jackappsdev.password_manager.shared.constants.ZERO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordItemDtoTest {

    // region default values

    @Test
    fun `default id is ZERO constant`() {
        val dto = PasswordItemDto(name = "App", username = "u", password = "p", notes = "")
        assertEquals(ZERO, dto.id)
    }

    @Test
    fun `default createdAt is close to current time`() {
        val before = System.currentTimeMillis()
        val dto = PasswordItemDto(name = "App", username = "u", password = "p", notes = "")
        val after = System.currentTimeMillis()

        assertTrue("createdAt should be within test execution window", dto.createdAt in before..after)
    }

    // endregion

    // region data class equality

    @Test
    fun `two DTOs with same fields are equal`() {
        val timestamp = 1_700_000_000L
        val dto1 = PasswordItemDto(id = 1, name = "A", username = "u", password = "p", notes = "n", createdAt = timestamp)
        val dto2 = PasswordItemDto(id = 1, name = "A", username = "u", password = "p", notes = "n", createdAt = timestamp)

        assertEquals(dto1, dto2)
    }

    @Test
    fun `DTOs with different ids are not equal`() {
        val dto1 = PasswordItemDto(id = 1, name = "A", username = "u", password = "p", notes = "", createdAt = 0L)
        val dto2 = PasswordItemDto(id = 2, name = "A", username = "u", password = "p", notes = "", createdAt = 0L)

        assertNotEquals(dto1, dto2)
    }

    @Test
    fun `DTOs with different names are not equal`() {
        val dto1 = PasswordItemDto(id = 1, name = "Google", username = "u", password = "p", notes = "", createdAt = 0L)
        val dto2 = PasswordItemDto(id = 1, name = "Apple", username = "u", password = "p", notes = "", createdAt = 0L)

        assertNotEquals(dto1, dto2)
    }

    @Test
    fun `DTOs with different passwords are not equal`() {
        val dto1 = PasswordItemDto(id = 1, name = "A", username = "u", password = "pass1", notes = "", createdAt = 0L)
        val dto2 = PasswordItemDto(id = 1, name = "A", username = "u", password = "pass2", notes = "", createdAt = 0L)

        assertNotEquals(dto1, dto2)
    }

    // endregion

    // region copy / immutability

    @Test
    fun `copy with updated name produces new DTO with same other fields`() {
        val original = PasswordItemDto(id = 5, name = "Old", username = "u", password = "p", notes = "n", createdAt = 100L)

        val copy = original.copy(name = "New")

        assertEquals("New", copy.name)
        assertEquals(5, copy.id)
        assertEquals("u", copy.username)
        assertEquals("p", copy.password)
        assertEquals("n", copy.notes)
        assertEquals(100L, copy.createdAt)
    }

    @Test
    fun `copy with updated password does not mutate original`() {
        val original = PasswordItemDto(id = 1, name = "A", username = "u", password = "original", notes = "", createdAt = 0L)

        original.copy(password = "changed")

        assertEquals("original", original.password)
    }

    // endregion

    // region field integrity

    @Test
    fun `all provided field values are stored correctly`() {
        val dto = PasswordItemDto(
            id = 42,
            name = "GitHub",
            username = "dev@github.com",
            password = "gh-token-xyz",
            notes = "Personal repo access",
            createdAt = 1_609_459_200L
        )

        assertEquals(42, dto.id)
        assertEquals("GitHub", dto.name)
        assertEquals("dev@github.com", dto.username)
        assertEquals("gh-token-xyz", dto.password)
        assertEquals("Personal repo access", dto.notes)
        assertEquals(1_609_459_200L, dto.createdAt)
    }

    @Test
    fun `empty strings are stored correctly for notes and username`() {
        val dto = PasswordItemDto(id = 1, name = "Service", username = "", password = "p", notes = "")

        assertEquals("", dto.username)
        assertEquals("", dto.notes)
    }

    // endregion
}
