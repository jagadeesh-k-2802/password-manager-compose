package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.annotation.StringRes

sealed interface EditPasswordItemError {
    data class NameError(@StringRes val error: Int) : EditPasswordItemError
}
