package com.jagadeesh.passwordmanager.presentation.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object PasswordGenerator : Routes("password-generator")
    data object Settings : Routes("settings")
    data object ItemDetail : Routes("item-detail/{id}") {
        fun getPath(id: String) = "item-detail/${id}"
    }
}
