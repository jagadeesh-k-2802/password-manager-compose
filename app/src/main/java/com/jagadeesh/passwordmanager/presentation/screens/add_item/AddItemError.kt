package com.jagadeesh.passwordmanager.presentation.screens.add_item

sealed interface AddItemError {
    data class NameError(val error: String) : AddItemError
}
