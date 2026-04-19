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

    // region Password

    @Test
    fun `hasPasswordSet returns true when password is not null or blank`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "secure-password"))
        assertTrue(repository.hasPasswordSet())
    }

    @Test
    fun `hasPasswordSet returns false when password is null`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = null))
        assertFalse(repository.hasPasswordSet())
    }

    @Test
    fun `hasPasswordSet returns false when password is blank`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "  "))
        assertFalse(repository.hasPasswordSet())
    }

    @Test
    fun `hasPasswordSet returns false when password is empty string`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = ""))
        assertFalse(repository.hasPasswordSet())
    }

    @Test
    fun `verifyPassword returns true when passwords match`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "correctPassword"))
        assertTrue(repository.verifyPassword("correctPassword"))
    }

    @Test
    fun `verifyPassword returns false when passwords do not match`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "correctPassword"))
        assertFalse(repository.verifyPassword("wrongPassword"))
    }

    @Test
    fun `verifyPassword is case sensitive`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "Password123"))
        assertFalse(repository.verifyPassword("password123"))
    }

    @Test
    fun `setInitialPassword updates dataStore with new password`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(password = "newPass")

        repository.setInitialPassword("newPass")

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region PIN

    @Test
    fun `hasPinSet returns true when pin is not null or blank`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "1234"))
        assertTrue(repository.hasPinSet())
    }

    @Test
    fun `hasPinSet returns false when pin is null`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = null))
        assertFalse(repository.hasPinSet())
    }

    @Test
    fun `hasPinSet returns false when pin is blank`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = ""))
        assertFalse(repository.hasPinSet())
    }

    @Test
    fun `verifyPin returns true when pins match`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "5678"))
        assertTrue(repository.verifyPin("5678"))
    }

    @Test
    fun `verifyPin returns false when pins do not match`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(pin = "5678"))
        assertFalse(repository.verifyPin("0000"))
    }

    @Test
    fun `setPin updates dataStore with new pin`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = "9999")

        repository.setPin("9999")

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region Screen Lock

    @Test
    fun `getScreenLockToUnlock returns true when enabled`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(useScreenLockToUnlock = true))
        assertTrue(repository.getScreenLockToUnlock())
    }

    @Test
    fun `getScreenLockToUnlock returns false when disabled`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(useScreenLockToUnlock = false))
        assertFalse(repository.getScreenLockToUnlock())
    }

    @Test
    fun `setUseScreenLockToUnlock updates dataStore`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(useScreenLockToUnlock = false)

        repository.setUseScreenLockToUnlock(false)

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region Incognito Keyboard

    @Test
    fun `getUseIncognitoKeyboard emits current value`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(useIncognitoKeyboard = true))

        val flow = repository.getUseIncognitoKeyboard()

        assertTrue(flow.first())
    }

    @Test
    fun `getUseIncognitoKeyboard emits false when disabled`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(useIncognitoKeyboard = false))

        val flow = repository.getUseIncognitoKeyboard()

        assertFalse(flow.first())
    }

    @Test
    fun `setUseIncognitoKeyboard updates dataStore`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(useIncognitoKeyboard = true)

        repository.setUseIncognitoKeyboard(true)

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region Dynamic Colors

    @Test
    fun `getUseDynamicColors emits true when enabled`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(useDynamicColors = true))

        val flow = repository.getUseDynamicColors()

        assertTrue(flow.first())
    }

    @Test
    fun `getUseDynamicColors emits false when disabled`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(useDynamicColors = false))

        assertFalse(repository.getUseDynamicColors().first())
    }

    @Test
    fun `setUseDynamicColors updates dataStore`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(useDynamicColors = false)

        repository.setUseDynamicColors(false)

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region Android Watch PIN

    @Test
    fun `hasAndroidWatchPinSet returns true when watch pin is set`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(androidWatchPin = "4321"))
        assertTrue(repository.hasAndroidWatchPinSet())
    }

    @Test
    fun `hasAndroidWatchPinSet returns false when watch pin is null`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(androidWatchPin = null))
        assertFalse(repository.hasAndroidWatchPinSet())
    }

    @Test
    fun `hasAndroidWatchPinSet returns false when watch pin is blank`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(androidWatchPin = ""))
        assertFalse(repository.hasAndroidWatchPinSet())
    }

    @Test
    fun `setAndroidWatchPinSet updates dataStore with new pin`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(androidWatchPin = "1111")

        repository.setAndroidWatchPinSet("1111")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `setAndroidWatchPinSet with null clears watch pin`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(androidWatchPin = null)

        repository.setAndroidWatchPinSet(null)

        coVerify { dataStore.updateData(any()) }
    }

    // endregion

    // region Auto Lock Delay

    @Test
    fun `getAutoLockDelayMs emits stored delay value`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(autoLockDelayMs = 60_000L))

        val result = repository.getAutoLockDelayMs().first()

        assertEquals(60_000L, result)
    }

    @Test
    fun `setAutoLockDelayMs updates dataStore`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(autoLockDelayMs = 30_000L)

        repository.setAutoLockDelayMs(30_000L)

        coVerify { dataStore.updateData(any()) }
    }

    // endregion
}
