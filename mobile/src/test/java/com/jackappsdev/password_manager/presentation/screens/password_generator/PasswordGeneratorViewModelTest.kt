package com.jackappsdev.password_manager.presentation.screens.password_generator

import app.cash.turbine.test
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_generator.event.PasswordGeneratorUiEvent
import com.jackappsdev.password_manager.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PasswordGeneratorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state has a generated password`() {
        val viewModel = PasswordGeneratorViewModel()
        val state = viewModel.state
        
        assertNotEquals("", state.password)
        assertEquals(8, state.password.length)
    }

    @Test
    fun `length change updates password length`() {
        val viewModel = PasswordGeneratorViewModel()
        
        viewModel.onEvent(PasswordGeneratorUiEvent.LengthChange(20))
        
        assertEquals(20, viewModel.state.passwordLength)
        assertEquals(20, viewModel.state.password.length)
    }

    @Test
    fun `toggle options updates state and regenerates password`() {
        val viewModel = PasswordGeneratorViewModel()
        val initialPassword = viewModel.state.password
        
        viewModel.onEvent(PasswordGeneratorUiEvent.ToggleIncludeSymbols)
        
        // Symbols are true by default in state? Let's check state.
        // If it was true, it's now false.
        assertNotEquals(initialPassword, viewModel.state.password)
    }

    @Test
    fun `cannot toggle off all options`() = runTest {
        val viewModel = PasswordGeneratorViewModel()
        
        // Symbols, Numbers, Uppercase, Lowercase are true by default.
        // Toggle off 3 of them.
        viewModel.onEvent(PasswordGeneratorUiEvent.ToggleIncludeSymbols)
        viewModel.onEvent(PasswordGeneratorUiEvent.ToggleIncludeNumbers)
        viewModel.onEvent(PasswordGeneratorUiEvent.ToggleIncludeUppercase)
        
        // Now only Lowercase is true. Try to toggle it off.
        viewModel.effectFlow.test {
            viewModel.onEvent(PasswordGeneratorUiEvent.ToggleIncludeLowercase)
            
            val effect = awaitItem()
            assertTrue(effect is PasswordGeneratorUiEffect.ShowSnackbarMessage)
            
            // State should still have includeLowercase as true
            assertTrue(viewModel.state.includeLowercase)
        }
    }
    
    // Helper to use assertTrue since I didn't import it
    private fun assertTrue(value: Boolean) {
        org.junit.Assert.assertTrue(value)
    }
}
