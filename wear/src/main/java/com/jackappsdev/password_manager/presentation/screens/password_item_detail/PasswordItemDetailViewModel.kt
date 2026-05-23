package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent
import com.jackappsdev.password_manager.shared.base.EventDrivenViewModel
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD_ID
import com.jackappsdev.password_manager.shared.constants.REMOVE_PASSWORD_FROM_WATCH
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel(), EventDrivenViewModel<PasswordItemDetailUiEvent, PasswordItemDetailUiEffect> {

    companion object {
        private const val TIMESTAMP = "timestamp"
    }

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

    private suspend fun deleteFromWatchOnly(): PasswordItemDetailUiEffect? {
        val passwordItem = state.passwordItem?.value ?: return null
        
        // 1. Delete from local Wear OS database
        passwordItemRepository.deletePasswordItem(passwordItem)

        // 2. Put Data Item to notify Mobile
        try {
            val putDataRequest = PutDataMapRequest.create(REMOVE_PASSWORD_FROM_WATCH).run {
                dataMap.putInt(KEY_PASSWORD_ID, passwordItem.id ?: -1)
                dataMap.putLong(TIMESTAMP, System.currentTimeMillis())
                asPutDataRequest()
            }
            Wearable.getDataClient(context).putDataItem(putDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return PasswordItemDetailUiEffect.PasswordRemoved
    }

    override fun onEvent(event: PasswordItemDetailUiEvent) {
        viewModelScope.launch {
            val effect = when (event) {
                is PasswordItemDetailUiEvent.ToggleAlreadySetOnce -> toggleAlreadySetOnce()
                is PasswordItemDetailUiEvent.NavigateUp -> PasswordItemDetailUiEffect.NavigateUp
                is PasswordItemDetailUiEvent.DeleteFromWatchOnly -> deleteFromWatchOnly()
            }

            if (effect is PasswordItemDetailUiEffect) _effectChannel.send(effect)
        }
    }
}
