package com.jagadeesh.passwordmanager.presentation.screens.category_item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadeesh.passwordmanager.domain.model.CategoryModel
import com.jagadeesh.passwordmanager.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val categoryItem = categoryRepository.getCategoryItem(id.toInt())

    fun hasChanges(name: String, color: String, categoryItem: CategoryModel?): Boolean {
        if (name != categoryItem?.name) return true
        if (color != categoryItem.color) return true
        return false
    }

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
                    createdAt = categoryModel?.createdAt
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
