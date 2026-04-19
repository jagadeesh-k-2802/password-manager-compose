package com.jackappsdev.password_manager.data.repository

import androidx.datastore.core.DataStore
import com.jackappsdev.password_manager.data.local.dao.PasswordDao
import com.jackappsdev.password_manager.data.models.UserSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PassphraseRepositoryImplTest {

    private lateinit var passwordDao: PasswordDao
    private lateinit var dataStore: DataStore<UserSettings>
    private lateinit var repository: PassphraseRepositoryImpl

    @Before
    fun setUp() {
        passwordDao = mockk(relaxed = true)
        dataStore = mockk()
        repository = PassphraseRepositoryImpl(passwordDao, dataStore)
    }

    @Test
    fun `updatePassword reads old password from dataStore before changing`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "oldPass"))
        coEvery { dataStore.updateData(any()) } returns UserSettings(password = "newPass")

        repository.updatePassword("newPass")

        coVerify { dataStore.data }
    }

    @Test
    fun `updatePassword updates dataStore with new password`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "oldPass"))
        coEvery { dataStore.updateData(any()) } returns UserSettings(password = "newPass")

        repository.updatePassword("newPass")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `updatePassword calls changePassword on dao with old and new passwords`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "oldSecret"))
        coEvery { dataStore.updateData(any()) } returns UserSettings(password = "newSecret")

        repository.updatePassword("newSecret")

        coVerify { passwordDao.changePassword("oldSecret", "newSecret") }
    }

    @Test
    fun `updatePassword updates dataStore before calling dao changePassword`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = "alpha"))
        coEvery { dataStore.updateData(any()) } returns UserSettings(password = "beta")

        repository.updatePassword("beta")

        coVerifyOrder {
            dataStore.updateData(any())
            passwordDao.changePassword("alpha", "beta")
        }
    }

    @Test
    fun `updatePassword passes null old password to changePassword when no password was set`() = runTest {
        every { dataStore.data } returns flowOf(UserSettings(password = null))
        coEvery { dataStore.updateData(any()) } returns UserSettings(password = "firstPass")

        repository.updatePassword("firstPass")

        coVerify { passwordDao.changePassword(null, "firstPass") }
    }
}
