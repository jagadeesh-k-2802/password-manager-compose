package com.jackappsdev.password_manager.autofill

import com.jackappsdev.password_manager.domain.model.PasswordItemModel

sealed class AutofillUiEffect {
    data class ItemSelected(val item: PasswordItemModel) : AutofillUiEffect()
}
