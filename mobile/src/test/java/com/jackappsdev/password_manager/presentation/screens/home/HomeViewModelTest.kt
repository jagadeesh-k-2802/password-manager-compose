package com.jackappsdev.password_manager.presentation.screens.home

import app.cash.turbine.test
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEffect
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEvent
import com.jackappsdev.password_manager.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val passwordItemRepository: PasswordItemRepository = mockk(relaxed = true)
    private val categoryRepository: CategoryRepository = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    // Both repositories must be stubbed before the ViewModel is constructed because
    // HomeViewModel.onInit() calls stateIn(viewModelScope) — the suspend variant that
    // waits for the first upstream emission. A relaxed mock returns an immediately-completing
    // empty flow, which causes NoSuchElementException at stateIn().
    @Before
    fun setUp() {
        every { passwordItemRepository.getPasswordItems(any(), any(), any()) } returns flowOf(emptyList())
        every { categoryRepository.getAllCategories() } returns flowOf(emptyList())
        viewModel = HomeViewModel(passwordItemRepository, categoryRepository)
    }

    @Test
    fun `initialization fetches items and categories`() = runTest {
        verify { passwordItemRepository.getPasswordItems(any(), any(), "") }
        verify { categoryRepository.getAllCategories() }
        assertEquals(false, viewModel.state.isLoading)
    }

    @Test
    fun `EnterSearchQuery updates state`() = runTest {
        viewModel.onEvent(HomeUiEvent.EnterSearchQuery("test"))

        assertEquals("test", viewModel.state.searchQuery)
        assertTrue(viewModel.state.isSearching)
    }

    @Test
    fun `ClearSearch resets search state`() = runTest {
        viewModel.onEvent(HomeUiEvent.EnterSearchQuery("test"))

        viewModel.effectFlow.test {
            viewModel.onEvent(HomeUiEvent.ClearSearch)

            assertEquals(HomeUiEffect.SearchCleared, awaitItem())
            assertEquals("", viewModel.state.searchQuery)
            assertTrue(!viewModel.state.isSearching)
        }
    }

    @Test
    fun `SelectFilterBy updates filter and fetches items`() = runTest {
        val filter = FilterBy.NoCategoryItems

        viewModel.effectFlow.test {
            viewModel.onEvent(HomeUiEvent.SelectFilterBy(filter))

            assertEquals(HomeUiEffect.FilterSelected, awaitItem())
            assertEquals(filter, viewModel.state.filterBy)
            verify { passwordItemRepository.getPasswordItems(any(), "category_id IS NULL", any()) }
        }
    }
}
