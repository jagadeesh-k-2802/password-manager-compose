package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        getPasswordItems()
    }

    private fun getPasswordItems() {
        viewModelScope.launch {
            if (state.isLoading) {
                state = state.copy(
                    isLoading = false,
                    items = passwordItemRepository.getPasswordItems().stateIn(viewModelScope)
                )
            }
        }
    }
}
