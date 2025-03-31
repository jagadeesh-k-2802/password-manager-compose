package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {

    private val passwordItemDetail = savedStateHandle.toRoute<Routes.PasswordItemDetail>()
    val passwordItem = passwordItemRepository.getPasswordItem(passwordItemDetail.id)

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

    fun toggleIsAddedToWatch(passwordWithCategoryModel: PasswordWithCategoryModel) {
        viewModelScope.launch {
            passwordItemRepository.insertPasswordItem(
                PasswordItemModel(
                    id = passwordWithCategoryModel.id,
                    name = passwordWithCategoryModel.name,
                    username = passwordWithCategoryModel.username,
                    password = passwordWithCategoryModel.password,
                    notes = passwordWithCategoryModel.notes,
                    categoryId = passwordWithCategoryModel.categoryId,
                    website = passwordWithCategoryModel.website,
                    isAddedToWatch = !passwordWithCategoryModel.isAddedToWatch,
                    createdAt = passwordWithCategoryModel.createdAt
                )
            )
        }
    }

    fun deleteItem(item: PasswordWithCategoryModel) {
        viewModelScope.launch {
            passwordItemRepository.deletePasswordItem(item)
        }
    }
}
