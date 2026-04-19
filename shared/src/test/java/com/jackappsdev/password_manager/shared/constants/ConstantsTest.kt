package com.jackappsdev.password_manager.shared.constants

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConstantsTest {

    @Test
    fun `EMPTY_STRING is an empty string`() {
        assertEquals("", EMPTY_STRING)
        assertTrue(EMPTY_STRING.isEmpty())
    }

    @Test
    fun `ZERO equals integer zero`() {
        assertEquals(0, ZERO)
    }

    @Test
    fun `WearOS path constants start with forward slash`() {
        assertTrue("SET_PIN must start with /", SET_PIN.startsWith("/"))
        assertTrue("UPSERT_PASSWORD must start with /", UPSERT_PASSWORD.startsWith("/"))
        assertTrue("DELETE_PASSWORD must start with /", DELETE_PASSWORD.startsWith("/"))
        assertTrue("WIPE_DATA must start with /", WIPE_DATA.startsWith("/"))
    }

    @Test
    fun `WearOS path constants are non-blank`() {
        assertTrue(SET_PIN.isNotBlank())
        assertTrue(UPSERT_PASSWORD.isNotBlank())
        assertTrue(DELETE_PASSWORD.isNotBlank())
        assertTrue(WIPE_DATA.isNotBlank())
    }

    @Test
    fun `all WearOS path constants are distinct`() {
        val paths = setOf(SET_PIN, UPSERT_PASSWORD, DELETE_PASSWORD, WIPE_DATA)
        assertEquals(4, paths.size)
    }

    @Test
    fun `PLAY_STORE_APP_URI is a valid Play Store URL`() {
        assertTrue(PLAY_STORE_APP_URI.startsWith("https://play.google.com"))
        assertTrue(PLAY_STORE_APP_URI.contains("com.jackappsdev.password_manager"))
    }

    @Test
    fun `KEY_PIN and KEY_PASSWORD are non-blank`() {
        assertTrue(KEY_PIN.isNotBlank())
        assertTrue(KEY_PASSWORD.isNotBlank())
    }

    @Test
    fun `KEY_PIN and KEY_PASSWORD are distinct`() {
        assertNotEquals(KEY_PIN, KEY_PASSWORD)
    }

    private fun assertNotEquals(a: String, b: String) {
        assertTrue("$a and $b should be distinct constants", a != b)
    }
}
