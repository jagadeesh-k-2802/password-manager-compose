package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.models.UserSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PassphraseRepositoryImplTest {

    private lateinit var dataStore: DataStore<UserSettings>
    private lateinit var repository: PassphraseRepositoryImpl

    @Before
    fun setUp() {
        dataStore = mockk()
        repository = PassphraseRepositoryImpl(dataStore)
    }

    @Test
    fun `updatePin updates dataStore with new pin`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = "newPin")

        repository.updatePin("newPin")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `updatePin calls dataStore updateData exactly once`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = "0000")

        repository.updatePin("0000")

        coVerify(exactly = 1) { dataStore.updateData(any()) }
    }

    @Test
    fun `updatePin with numeric pin delegates to dataStore`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = "1234")

        repository.updatePin("1234")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `updatePin with alphanumeric pin delegates to dataStore`() = runTest {
        coEvery { dataStore.updateData(any()) } returns UserSettings(pin = "abc123")

        repository.updatePin("abc123")

        coVerify { dataStore.updateData(any()) }
    }
}
