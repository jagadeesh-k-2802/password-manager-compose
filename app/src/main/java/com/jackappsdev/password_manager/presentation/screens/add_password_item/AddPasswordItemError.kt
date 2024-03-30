package com.jackappsdev.password_manager.presentation.screens.add_password_item

sealed interface AddPasswordItemError {
    data class NameError(val error: String) : AddPasswordItemError
}
