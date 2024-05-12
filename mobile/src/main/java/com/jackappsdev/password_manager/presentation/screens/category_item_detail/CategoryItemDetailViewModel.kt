package com.jackappsdev.password_manager.presentation.screens.category_item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val categoryItem = categoryRepository.getCategoryItem(id.toInt()).filterNotNull()

    fun onEditComplete(
        name: String,
        color: String,
        categoryModel: CategoryModel?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // It will update because onConflict is set to Replace
            categoryRepository.insertCategoryItem(
                CategoryModel(
                    id = categoryModel?.id,
                    name = name,
                    color = color,
                    createdAt = System.currentTimeMillis()
                )
            )

            onSuccess()
        }
    }

    fun deleteItem(item: CategoryModel) {
        viewModelScope.launch {
            categoryRepository.deleteCategoryItem(item)
        }
    }
}
