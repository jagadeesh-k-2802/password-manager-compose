package com.jackappsdev.password_manager.autofill

import com.jackappsdev.password_manager.domain.model.PasswordItemModel

sealed class AutofillUiEvent {
    data class EnterSearchQuery(val query: String) : AutofillUiEvent()
    data object ClearSearch : AutofillUiEvent()
    data class ItemSelected(val item: PasswordItemModel) : AutofillUiEvent()
}
