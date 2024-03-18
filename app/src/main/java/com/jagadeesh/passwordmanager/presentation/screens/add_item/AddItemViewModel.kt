package com.jagadeesh.passwordmanager.presentation.screens.add_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    fun addPasswordItem(
        name: String,
        username: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            passwordItemRepository.insertPasswordItem(
                PasswordItemModel(
                    name = name,
                    username = username,
                    password = password
                )
            )

            onSuccess()
        }
    }
}
