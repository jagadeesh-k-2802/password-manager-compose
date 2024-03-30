package com.jackappsdev.password_manager.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var useDynamicColors by mutableStateOf<Boolean?>(null)

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUseDynamicColors().collect {
                useDynamicColors = it
            }
        }
    }
}
