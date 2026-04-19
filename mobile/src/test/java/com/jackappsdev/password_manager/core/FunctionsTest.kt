package com.jackappsdev.password_manager.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FunctionsTest {

    @Test
    fun `generateRandomPassword includes only lowercase when configured`() {
        val config = GeneratePasswordConfig(
            length = 10,
            includeLowercase = true,
            includeUppercase = false,
            includeNumbers = false,
            includeSymbols = false
        )
        val password = generateRandomPassword(config)
        
        assertEquals(10, password.length)
        assertTrue(password.all { it in 'a'..'z' })
    }

    @Test
    fun `generateRandomPassword includes only uppercase when configured`() {
        val config = GeneratePasswordConfig(
            length = 12,
            includeLowercase = false,
            includeUppercase = true,
            includeNumbers = false,
            includeSymbols = false
        )
        val password = generateRandomPassword(config)
        
        assertEquals(12, password.length)
        assertTrue(password.all { it in 'A'..'Z' })
    }

    @Test
    fun `generateRandomPassword includes only numbers when configured`() {
        val config = GeneratePasswordConfig(
            length = 8,
            includeLowercase = false,
            includeUppercase = false,
            includeNumbers = true,
            includeSymbols = false
        )
        val password = generateRandomPassword(config)
        
        assertEquals(8, password.length)
        assertTrue(password.all { it in '0'..'9' })
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateRandomPassword throws exception when no character sets are included`() {
        val config = GeneratePasswordConfig(
            length = 8,
            includeLowercase = false,
            includeUppercase = false,
            includeNumbers = false,
            includeSymbols = false
        )
        generateRandomPassword(config)
    }

    @Test
    fun `generateRandomPassword includes additional characters when provided`() {
        val config = GeneratePasswordConfig(
            length = 20,
            includeLowercase = false,
            includeUppercase = false,
            includeNumbers = false,
            includeSymbols = false,
            additionalCharacters = "XY"
        )
        val password = generateRandomPassword(config)
        
        assertEquals(20, password.length)
        assertTrue(password.all { it == 'X' || it == 'Y' })
    }
}
