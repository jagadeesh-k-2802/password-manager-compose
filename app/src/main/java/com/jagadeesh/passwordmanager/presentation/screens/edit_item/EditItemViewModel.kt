package com.jagadeesh.passwordmanager.presentation.screens.edit_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val passwordItem = passwordItemRepository.getPasswordItem(id.toInt())

    fun hasChanges(
        name: String,
        username: String,
        password: String,
        notes: String,
        passwordItemModel: PasswordItemModel?
    ): Boolean {
        if (name != passwordItemModel?.name) return true
        if (username != passwordItemModel.username) return true
        if (password != passwordItemModel.password) return true
        if (notes != passwordItemModel.notes) return true
        return false
    }

    fun onEditComplete(
        name: String,
        username: String,
        password: String,
        notes: String,
        passwordItemModel: PasswordItemModel?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // It will update because onConflict is set to Replace
            passwordItemRepository.insertPasswordItem(
                PasswordItemModel(
                    id = passwordItemModel?.id,
                    name = name,
                    username = username,
                    password = password,
                    notes = notes,
                    createdAt = passwordItemModel?.createdAt
                )
            )

            onSuccess()
        }
    }
}
