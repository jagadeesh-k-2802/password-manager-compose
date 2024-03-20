package com.jagadeesh.passwordmanager.presentation.navigation

sealed class Routes(val route: String) {
    data object PasswordLock : Routes("password-lock")
    data object Home : Routes("home")
    data object AddItem : Routes("add-item")
    data object PasswordGenerator : Routes("password-generator")
    data object Settings : Routes("settings")
    data object ChangePassword : Routes("change-password")
    data object ItemDetail : Routes("item-detail/{id}") {
        fun getPath(id: Int) = route.replace("{id}", id.toString())
    }
    data object EditItem : Routes("edit-item/{id}") {
        fun getPath(id: Int) = route.replace("{id}", id.toString())
    }
}
