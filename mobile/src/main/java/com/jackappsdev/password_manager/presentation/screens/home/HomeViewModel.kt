package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.SortBy
import com.jackappsdev.password_manager.domain.model.orderBy
import com.jackappsdev.password_manager.domain.model.where
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEffect
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel(), EventDrivenViewModel<HomeUiEvent, HomeUiEffect> {

    var state by mutableStateOf(HomeState())
        private set

    private val _effectChannel = Channel<HomeUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            if (state.isLoading) {
                val itemsFlow = passwordItemRepository.getPasswordItems(
                    state.sortBy.orderBy(),
                    state.filterBy.where(),
                    EMPTY_STRING
                )

                val categoryFlow = categoryRepository.getAllCategories()

                state = state.copy(
                    isLoading = false,
                    items = itemsFlow.stateIn(viewModelScope),
                    categoryItems = categoryFlow.stateIn(viewModelScope)
                )
            }
        }
    }

    fun lockApplication() {
        Runtime.getRuntime().exit(0)
    }

    private fun onEnterSearchQuery(query: String) {
        state = state.copy(searchQuery = query, isSearching = query.isNotEmpty())
    }

    private fun onClearSearch(): HomeUiEffect {
        state = state.copy(searchQuery = EMPTY_STRING, isSearching = false, filteredItems = null)
        return HomeUiEffect.SearchCleared
    }

    private suspend fun searchItems() {
        state = if (state.searchQuery.isEmpty()) {
            state.copy(filteredItems = null)
        } else {
            val filteredItemsFlow = passwordItemRepository.getPasswordItems(
                state.sortBy.orderBy(),
                state.filterBy.where(),
                state.searchQuery.trim()
            )

            state.copy(
                filteredItems = filteredItemsFlow.stateIn(viewModelScope)
            )
        }
    }

    private suspend fun selectFilterBy(filterBy: FilterBy): HomeUiEffect {
        val itemsFlow = passwordItemRepository.getPasswordItems(
            state.sortBy.orderBy(),
            filterBy.where(),
            EMPTY_STRING
        )

        state = state.copy(
            filterBy = filterBy,
            items = itemsFlow.stateIn(viewModelScope)
        )

        if (state.searchQuery.isNotEmpty()) { searchItems() }
        return HomeUiEffect.FilterSelected
    }

    private suspend fun selectSortBy(sortBy: SortBy): HomeUiEffect {
        val itemsFlow = passwordItemRepository.getPasswordItems(
            sortBy.orderBy(),
            state.filterBy.where(),
            EMPTY_STRING
        )

        state = state.copy(
            sortBy = sortBy,
            items = itemsFlow.stateIn(viewModelScope)
        )

        if (state.searchQuery.isNotEmpty()) { searchItems() }
        return HomeUiEffect.SortSelected
    }

    override fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is HomeUiEvent.LockApplication -> lockApplication()
                is HomeUiEvent.EnterSearchQuery -> onEnterSearchQuery(event.query)
                is HomeUiEvent.ClearSearch -> onClearSearch()
                is HomeUiEvent.SearchItems -> searchItems()
                is HomeUiEvent.SelectFilterBy -> selectFilterBy(event.filterBy)
                is HomeUiEvent.SelectSortBy -> selectSortBy(event.sortBy)
                is HomeUiEvent.Search -> HomeUiEffect.Search
                is HomeUiEvent.ScrollToTop -> HomeUiEffect.ScrollToTop
                is HomeUiEvent.ToggleSortSheetVisibility -> HomeUiEffect.ToggleSortSheetVisibility
                is HomeUiEvent.ToggleFilterSheetVisibility -> HomeUiEffect.ToggleFilterSheetVisibility
                is HomeUiEvent.NavigateToPasswordItem -> HomeUiEffect.NavigateToPasswordItem(event.id)
                is HomeUiEvent.NavigateToAddPassword -> HomeUiEffect.NavigateToAddPassword
            }

            if (effect is HomeUiEffect) _effectChannel.send(effect)
        }
    }
}
