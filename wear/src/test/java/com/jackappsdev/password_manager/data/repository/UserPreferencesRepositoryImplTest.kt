package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.models.UserSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserPreferencesRepositoryImplTest {

    private lateinit var dataStore: DataStore<UserSettings>
    private lateinit var repository: UserPreferencesRepositoryImpl

    @Before
    fun setUp() {
        dataStore = mockk()
        repository = UserPreferencesRepositoryImpl(dataStore)
    }

    // region hasPinSet

    @Test
    fun `hasPinSet returns true when pin is set`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "1234"))
        assertTrue(repository.hasPinSet())
    }

    @Test
    fun `hasPinSet returns false when pin is null`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = null))
        assertFalse(repository.hasPinSet())
    }

    @Test
    fun `hasPinSet returns false when pin is empty string`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = ""))
        assertFalse(repository.hasPinSet())
    }

    @Test
    fun `hasPinSet returns false when pin is blank`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "   "))
        assertFalse(repository.hasPinSet())
    }

    // endregion

    // region verifyPin

    @Test
    fun `verifyPin returns true when pin matches stored pin`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "5678"))
        assertTrue(repository.verifyPin("5678"))
    }

    @Test
    fun `verifyPin returns false when pin does not match stored pin`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "5678"))
        assertFalse(repository.verifyPin("0000"))
    }

    @Test
    fun `verifyPin is case and character sensitive`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "ABCD"))
        assertFalse(repository.verifyPin("abcd"))
    }

    // endregion

    // region setPin

    @Test
    fun `setPin updates dataStore with new pin value`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = "9876")

        repository.setPin("9876")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `setPin with null clears the pin`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = null)

        repository.setPin(null)

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region listenForPin

    @Test
    fun `listenForPin emits current pin value`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "3456"))

        val result = repository.listenForPin().first()

        assertEquals("3456", result)
    }

    @Test
    fun `listenForPin emits null when no pin is set`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = null))

        val result = repository.listenForPin().first()

        assertNull(result)
    }

    // endregion
}
