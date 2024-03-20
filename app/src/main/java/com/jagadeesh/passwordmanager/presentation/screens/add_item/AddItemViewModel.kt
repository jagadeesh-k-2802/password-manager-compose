package com.jagadeesh.passwordmanager.presentation.screens.add_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository
) : ViewModel() {
    val errorChannel = Channel<AddItemError>()

    fun addPasswordItem(
        name: String,
        username: String,
        password: String,
        notes: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                errorChannel.send(AddItemError.NameError("Name should not be empty"))
            } else {
                passwordItemRepository.insertPasswordItem(
                    PasswordItemModel(
                        name = name,
                        username = username,
                        password = password,
                        notes = notes
                    )
                )

                withContext(Dispatchers.Main) { onSuccess() }
            }
        }
    }
}
