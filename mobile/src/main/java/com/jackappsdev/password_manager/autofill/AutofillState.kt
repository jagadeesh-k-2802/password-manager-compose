package com.jackappsdev.password_manager.autofill

import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import kotlinx.coroutines.flow.StateFlow

data class AutofillState(
    val items: StateFlow<List<PasswordItemModel>>? = null,
    val filteredItems: StateFlow<List<PasswordItemModel>>? = null,
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)
