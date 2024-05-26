package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.PasswordCategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val passwordItem = passwordItemRepository.getPasswordItem(id.toInt())

    var state by mutableStateOf(PasswordItemDetailState())
        private set

    init {
        getInitialData()
    }

    private fun getInitialData() {
        viewModelScope.launch {
            state = state.copy(
                hasAndroidWatchPinSet = userPreferencesRepository.hasAndroidWatchPinSet(),
            )
        }
    }

    fun toggleIsAddedToWatch(passwordCategoryModel: PasswordCategoryModel) {
        viewModelScope.launch {
            // It will update because onConflict is set to Replace
            passwordItemRepository.insertPasswordItem(
                PasswordItemModel(
                    id = passwordCategoryModel.id,
                    name = passwordCategoryModel.name,
                    username = passwordCategoryModel.username,
                    password = passwordCategoryModel.password,
                    notes = passwordCategoryModel.notes,
                    categoryId = passwordCategoryModel.categoryId,
                    website = passwordCategoryModel.website,
                    isAddedToWatch = !passwordCategoryModel.isAddedToWatch,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteItem(item: PasswordCategoryModel) {
        viewModelScope.launch {
            passwordItemRepository.deletePasswordItem(item)
        }
    }
}
