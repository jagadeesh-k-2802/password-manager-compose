package com.jagadeesh.passwordmanager.presentation.screens.add_password_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.CategoryModel
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel
import com.jagadeesh.passwordmanager.domain.repository.CategoryRepository
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPasswordItemViewModel @Inject constructor(
    private val passwordItemRepository: PasswordItemRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    val errorChannel = Channel<AddPasswordItemError>()
    val categoryItems = categoryRepository.getAllCategories()

    fun addPasswordItem(
        name: String,
        username: String,
        password: String,
        notes: String,
        categoryModel: CategoryModel,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                errorChannel.send(AddPasswordItemError.NameError("Name should not be empty"))
            } else {
                passwordItemRepository.insertPasswordItem(
                    PasswordItemModel(
                        name = name,
                        username = username,
                        password = password,
                        notes = notes,
                        categoryId = categoryModel.id
                    )
                )

                withContext(Dispatchers.Main) { onSuccess() }
            }
        }
    }
}
