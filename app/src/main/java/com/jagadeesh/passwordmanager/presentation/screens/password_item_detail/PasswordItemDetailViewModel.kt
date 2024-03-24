package com.jagadeesh.passwordmanager.presentation.screens.password_item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.PasswordCategoryModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val passwordItem = passwordItemRepository.getPasswordItem(id.toInt())

    fun deleteItem(item: PasswordCategoryModel) {
        viewModelScope.launch {
            passwordItemRepository.deletePasswordItem(item)
        }
    }
}
