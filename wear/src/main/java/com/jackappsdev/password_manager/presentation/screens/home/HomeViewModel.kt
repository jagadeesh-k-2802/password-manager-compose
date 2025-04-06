package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEffect
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
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
            val items = passwordItemRepository.getPasswordItems().stateIn(viewModelScope)

            if (state.isLoading) {
                state = state.copy(isLoading = false, items = items)
            }
        }
    }

    override fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is HomeUiEvent.NavigateToPasswordDetail -> HomeUiEffect.NavigateToPasswordDetail(event.id)
            }

            _effectChannel.send(effect)
        }
    }
}
