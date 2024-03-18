package com.jagadeesh.passwordmanager.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    // TODO: orderBy not working
    var passwordItems = passwordItemRepository.getPasswordItems(SortBy.ALPHABET_ASCENDING.orderBy())

    var state by mutableStateOf(HomeState())
        private set

    fun setSortBy(sortBy: SortBy) {
        state = state.copy(sortBy = sortBy)
        println(sortBy.orderBy())
        passwordItems = passwordItemRepository.getPasswordItems(sortBy.orderBy())
    }
}
