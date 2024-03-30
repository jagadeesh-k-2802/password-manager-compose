package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackappsdev.password_manager.domain.model.PasswordCategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditPasswordItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val passwordItemRepository: PasswordItemRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    val passwordItem = passwordItemRepository.getPasswordItem(id.toInt())
    val categoryItems = categoryRepository.getAllCategories()

    fun onEditComplete(
        name: String,
        username: String,
        password: String,
        notes: String,
        categoryId: Int?,
        passwordItemModel: PasswordCategoryModel?,
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
                    categoryId = categoryId,
                    createdAt = System.currentTimeMillis()
                )
            )

            withContext(Dispatchers.Main) { onSuccess() }
        }
    }
}
