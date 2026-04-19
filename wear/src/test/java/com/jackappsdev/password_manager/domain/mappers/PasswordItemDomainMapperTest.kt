package com.jackappsdev.password_manager.domain.mappers

import com.jackappsdev.password_manager.shared.data.dto.PasswordItemDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordItemDomainMapperTest {

    @Test
    fun `toPasswordItemModel maps all fields from DTO correctly`() {
        val dto = PasswordItemDto(
            id = 7,
            name = "LinkedIn",
            username = "professional@linkedin.com",
            password = "network123",
            notes = "Career account",
            createdAt = 1_600_000_000L
        )

        val model = dto.toPasswordItemModel()

        assertEquals(7, model.id)
        assertEquals("LinkedIn", model.name)
        assertEquals("professional@linkedin.com", model.username)
        assertEquals("network123", model.password)
        assertEquals("Career account", model.notes)
        assertEquals(1_600_000_000L, model.createdAt)
    }

    @Test
    fun `toPasswordItemModel maps zero id from DTO`() {
        val dto = PasswordItemDto(
            id = 0, name = "New", username = "u", password = "p", notes = "", createdAt = 0L
        )

        val model = dto.toPasswordItemModel()

        assertEquals(0, model.id)
        assertEquals(0L, model.createdAt)
    }

    @Test
    fun `toPasswordItemModel maps empty notes correctly`() {
        val dto = PasswordItemDto(id = 1, name = "App", username = "u", password = "p", notes = "")

        val model = dto.toPasswordItemModel()

        assertEquals("", model.notes)
    }

    @Test
    fun `toPasswordItemModel preserves exact password value`() {
        val complexPassword = "P@\$\$w0rd!#%^&*()"
        val dto = PasswordItemDto(id = 1, name = "Secure", username = "u", password = complexPassword, notes = "")

        val model = dto.toPasswordItemModel()

        assertEquals(complexPassword, model.password)
    }

    @Test
    fun `toPasswordItemModel preserves createdAt timestamp precisely`() {
        val timestamp = 1_700_123_456_789L
        val dto = PasswordItemDto(id = 1, name = "n", username = "u", password = "p", notes = "", createdAt = timestamp)

        val model = dto.toPasswordItemModel()

        assertEquals(timestamp, model.createdAt)
    }
}
