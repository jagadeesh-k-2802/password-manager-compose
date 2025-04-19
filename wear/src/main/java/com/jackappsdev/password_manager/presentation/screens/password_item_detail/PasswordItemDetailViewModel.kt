package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel(), EventDrivenViewModel<PasswordItemDetailUiEvent, PasswordItemDetailUiEffect> {

    var state by mutableStateOf(PasswordItemDetailState())
        private set

    private val _effectChannel = Channel<PasswordItemDetailUiEffect>()
    override val effectFlow = _effectChannel.receiveAsFlow()

    private val id: String = checkNotNull(savedStateHandle["id"])

    init {
        onInit()
    }

    private fun onInit() {
        viewModelScope.launch {
            val passwordItem = passwordItemRepository.getPasswordItem(id.toInt())
            state = state.copy(passwordItem = passwordItem.stateIn(viewModelScope))
        }
    }

    private fun toggleAlreadySetOnce() {
        state = state.copy(isValueAlreadySetOnce = !state.isValueAlreadySetOnce)
    }

    override fun onEvent(event: PasswordItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordItemDetailUiEvent.ToggleAlreadySetOnce -> toggleAlreadySetOnce()
                is PasswordItemDetailUiEvent.NavigateUp -> PasswordItemDetailUiEffect.NavigateUp
            }

            if (effect is PasswordItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
