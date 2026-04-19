package com.jackappsdev.password_manager.presentation.screens.password_lock

import app.cash.turbine.test
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PasswordLockViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)

    @Test
    fun `initial state sets hasPinSet correctly`() = runTest {
        coEvery { userPreferencesRepository.hasPinSet() } returns true
        
        val viewModel = PasswordLockViewModel(userPreferencesRepository)
        
        assertTrue(viewModel.state.hasPinSet == true)
    }

    @Test
    fun `number press updates pin in state`() = runTest {
        val viewModel = PasswordLockViewModel(userPreferencesRepository)
        
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("1"))
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("2"))
        
        assertEquals("12", viewModel.state.pin)
    }

    @Test
    fun `backspace removes last character from pin`() = runTest {
        val viewModel = PasswordLockViewModel(userPreferencesRepository)
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("1"))
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("2"))
        
        viewModel.onEvent(PasswordLockUiEvent.BackSpacePress)
        
        assertEquals("1", viewModel.state.pin)
    }

    @Test
    fun `verifyPin success updates state and sends effect`() = runTest {
        val viewModel = PasswordLockViewModel(userPreferencesRepository)
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("1"))
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("2"))
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("3"))
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("4"))
        
        coEvery { userPreferencesRepository.verifyPin("1234") } returns true
        
        viewModel.effectFlow.test {
            viewModel.onEvent(PasswordLockUiEvent.VerifyPin)
            
            assertEquals(PasswordLockUiEffect.Unlock, awaitItem())
            assertTrue(viewModel.state.hasBeenUnlocked)
            assertEquals("", viewModel.state.pin)
        }
    }

    @Test
    fun `verifyPin failure clears pin and sends effect`() = runTest {
        val viewModel = PasswordLockViewModel(userPreferencesRepository)
        viewModel.onEvent(PasswordLockUiEvent.NumberPress("1"))
        
        coEvery { userPreferencesRepository.verifyPin(any()) } returns false
        
        viewModel.effectFlow.test {
            viewModel.onEvent(PasswordLockUiEvent.VerifyPin)
            
            assertEquals(PasswordLockUiEffect.IncorrectPassword, awaitItem())
            assertFalse(viewModel.state.hasBeenUnlocked)
            assertEquals("", viewModel.state.pin)
        }
    }
}
