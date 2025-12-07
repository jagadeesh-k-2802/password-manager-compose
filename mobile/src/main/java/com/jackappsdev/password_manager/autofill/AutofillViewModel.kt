package com.jackappsdev.password_manager.autofill

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.domain.model.SortBy
import com.jackappsdev.password_manager.domain.model.orderBy
import com.jackappsdev.password_manager.domain.model.where
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutofillViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel(), EventDrivenViewModel<AutofillUiEvent, AutofillUiEffect> {

    var state by mutableStateOf(AutofillState())
        private set

    private val _effectChannel = Channel<AutofillUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            if (state.isLoading) {
                val itemsFlow = passwordItemRepository.getPasswordItems(
                    SortBy.ALPHABET_ASCENDING.orderBy(),
                    FilterBy.All.where(),
                    EMPTY_STRING
                )

                state = state.copy(
                    isLoading = false,
                    items = itemsFlow.stateIn(viewModelScope)
                )
            }
        }
    }

    private fun onEnterSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
        viewModelScope.launch {
            if (query.isEmpty()) {
                state = state.copy(filteredItems = null)
            } else {
                val filteredItemsFlow = passwordItemRepository.getPasswordItems(
                    SortBy.ALPHABET_ASCENDING.orderBy(),
                    FilterBy.All.where(),
                    query.trim()
                )

                state = state.copy(
                    filteredItems = filteredItemsFlow.stateIn(viewModelScope)
                )
            }
        }
    }

    private fun onClearSearch() {
        state = state.copy(searchQuery = EMPTY_STRING, filteredItems = null)
    }

    override fun onEvent(event: AutofillUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is AutofillUiEvent.EnterSearchQuery -> onEnterSearchQuery(event.query)
                is AutofillUiEvent.ClearSearch -> onClearSearch()
                is AutofillUiEvent.ItemSelected -> AutofillUiEffect.ItemSelected(event.item)
            }

            if (effect is AutofillUiEffect) _effectChannel.send(effect)
        }
    }
}
