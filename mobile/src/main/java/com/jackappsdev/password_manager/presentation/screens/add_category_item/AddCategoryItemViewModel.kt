package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddCategoryItemViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    val errorChannel = Channel<AddCategoryItemError>()

    fun addItem(name: String, color: String, onSuccess: (CategoryModel) -> Unit) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                errorChannel.send(AddCategoryItemError.NameError("Name should not be empty"))
            } else {
                val model = CategoryModel(name = name, color = color)
                val id = categoryRepository.insertCategoryItem(model)
                withContext(Dispatchers.Main) { onSuccess(model.copy(id = id.toInt())) }
            }
        }
    }
}
