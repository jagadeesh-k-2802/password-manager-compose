package com.jagadeesh.passwordmanager.presentation.screens.add_category_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.CategoryModel
import com.jagadeesh.passwordmanager.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryItemViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    val errorChannel = Channel<AddCategoryItemError>()

    fun addItem(name: String, color: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                errorChannel.send(AddCategoryItemError.NameError("Name should not be empty"))
            } else {
                categoryRepository.insertCategoryItem(CategoryModel(name = name, color = color))
                onSuccess()
            }
        }
    }
}
