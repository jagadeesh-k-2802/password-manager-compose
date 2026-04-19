package com.jackappsdev.password_manager.data.repository

import com.jackappsdev.password_manager.data.local.PasswordDao
import com.jackappsdev.password_manager.data.local.PasswordItemEntity
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordItemRepositoryImplTest {

    private lateinit var passwordDao: PasswordDao
    private lateinit var repository: PasswordItemRepositoryImpl

    @Before
    fun setUp() {
        passwordDao = mockk()
        repository = PasswordItemRepositoryImpl(passwordDao)
    }

    // region getPasswordItems

    @Test
    fun `getPasswordItems maps entity list to model list`() = runTest {
        val entities = listOf(
            PasswordItemEntity(id = 1, name = "Gmail", username = "user@gmail.com", password = "g!", notes = "", createdAt = 100L),
            PasswordItemEntity(id = 2, name = "Outlook", username = "user@outlook.com", password = "o!", notes = "", createdAt = 200L)
        )
        every { passwordDao.getAllPasswordEntities() } returns flowOf(entities)

        val result = repository.getPasswordItems().first()

        assertEquals(2, result.size)
        assertEquals("Gmail", result[0].name)
        assertEquals("Outlook", result[1].name)
    }

    @Test
    fun `getPasswordItems emits empty list when no passwords exist`() = runTest {
        every { passwordDao.getAllPasswordEntities() } returns flowOf(emptyList())

        val result = repository.getPasswordItems().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getPasswordItems maps all fields from entity`() = runTest {
        val entity = PasswordItemEntity(
            id = 5, name = "Dropbox", username = "cloud@dropbox.com",
            password = "drop!", notes = "Cloud storage", createdAt = 500L
        )
        every { passwordDao.getAllPasswordEntities() } returns flowOf(listOf(entity))

        val result = repository.getPasswordItems().first()

        assertEquals(5, result[0].id)
        assertEquals("Dropbox", result[0].name)
        assertEquals("cloud@dropbox.com", result[0].username)
        assertEquals("drop!", result[0].password)
        assertEquals("Cloud storage", result[0].notes)
        assertEquals(500L, result[0].createdAt)
    }

    // endregion

    // region getPasswordItem

    @Test
    fun `getPasswordItem returns mapped model for given id`() = runTest {
        val entity = PasswordItemEntity(
            id = 3, name = "Spotify", username = "music@spotify.com",
            password = "sp!", notes = "", createdAt = 300L
        )
        every { passwordDao.getPasswordEntity(3) } returns flowOf(entity)

        val result = repository.getPasswordItem(3).first()

        assertEquals(3, result?.id)
        assertEquals("Spotify", result?.name)
    }

    @Test
    fun `getPasswordItem returns null when entity not found`() = runTest {
        every { passwordDao.getPasswordEntity(999) } returns flowOf(null)

        val result = repository.getPasswordItem(999).first()

        assertNull(result)
    }

    // endregion

    // region upsertPasswordItem

    @Test
    fun `upsertPasswordItem delegates to dao with correct entity`() = runTest {
        val model = PasswordItemModel(
            id = 10, name = "Netflix", username = "viewer@netflix.com",
            password = "nf!", notes = "Streaming", createdAt = 1000L
        )
        coEvery { passwordDao.upsertPasswordEntity(any()) } returns Unit

        repository.upsertPasswordItem(model)

        coVerify {
            passwordDao.upsertPasswordEntity(match { entity ->
                entity.id == 10 && entity.name == "Netflix" && entity.username == "viewer@netflix.com"
            })
        }
    }

    @Test
    fun `upsertPasswordItem for new model uses zero id`() = runTest {
        val model = PasswordItemModel(
            id = null, name = "New App", username = "u", password = "p", notes = ""
        )
        coEvery { passwordDao.upsertPasswordEntity(any()) } returns Unit

        repository.upsertPasswordItem(model)

        coVerify { passwordDao.upsertPasswordEntity(match { it.id == 0 }) }
    }

    // endregion

    // region deletePasswordItem

    @Test
    fun `deletePasswordItem delegates to dao with correct entity`() = runTest {
        val model = PasswordItemModel(
            id = 7, name = "Twitter", username = "tweet@twitter.com",
            password = "tw!", notes = "", createdAt = 700L
        )
        coEvery { passwordDao.deletePasswordEntity(any()) } returns Unit

        repository.deletePasswordItem(model)

        coVerify {
            passwordDao.deletePasswordEntity(match { entity ->
                entity.id == 7 && entity.name == "Twitter"
            })
        }
    }

    // endregion

    // region deleteAllPasswords

    @Test
    fun `deleteAllPasswords delegates to dao`() = runTest {
        coEvery { passwordDao.deleteAllPasswords() } returns Unit

        repository.deleteAllPasswords()

        coVerify { passwordDao.deleteAllPasswords() }
    }

    // endregion
}
