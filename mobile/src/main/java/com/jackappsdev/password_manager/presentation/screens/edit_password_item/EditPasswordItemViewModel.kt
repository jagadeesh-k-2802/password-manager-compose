package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.PasswordCategoryModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.CategoryRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditPasswordItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val passwordItemRepository: PasswordItemRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val editPasswordItem = savedStateHandle.toRoute<Routes.EditPasswordItem>()
    val errorChannel = Channel<EditPasswordItemError>()
    val passwordItem = passwordItemRepository.getPasswordItem(editPasswordItem.id)
    val categoryItems = categoryRepository.getAllCategories()

    fun onEditComplete(
        name: String,
        username: String,
        password: String,
        website: String,
        notes: String,
        categoryId: Int?,
        passwordItemModel: PasswordCategoryModel?,
        onSuccess: (PasswordItemModel) -> Unit
    ) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                errorChannel.send(EditPasswordItemError.NameError(R.string.error_name_not_empty))
            } else {
                // It will update because onConflict is set to Replace
                val newPasswordItemModel = PasswordItemModel(
                    id = passwordItemModel?.id,
                    name = name,
                    username = username,
                    password = password,
                    website = website,
                    notes = notes,
                    categoryId = categoryId,
                    isAddedToWatch = false,
                    createdAt = System.currentTimeMillis()
                )

                passwordItemRepository.insertPasswordItem(newPasswordItemModel)
                withContext(Dispatchers.Main) { onSuccess(newPasswordItemModel) }
            }
        }
    }
}
