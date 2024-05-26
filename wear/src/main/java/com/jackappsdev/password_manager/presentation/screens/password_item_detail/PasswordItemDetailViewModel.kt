package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val passwordItem = passwordItemRepository.getPasswordItem(id.toInt())
}
