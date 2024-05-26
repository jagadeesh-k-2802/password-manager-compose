package com.jackappsdev.password_manager.presentation.navigation

sealed class Routes(val route: String) {
    data object PasswordLock : Routes("password-lock")
    data object Home : Routes("home")
    data object AddPasswordItem : Routes("add-password-item")
    data object PasswordGenerator : Routes("password-generator")
    data object Settings : Routes("settings")
    data object ChangePassword : Routes("change-password")
    data object AndroidWatch : Routes("android-watch")
    data object ManageCategories: Routes("manage-categories")
    data object PasswordItemDetail : Routes("password-item-detail/{id}") {
        fun getPath(id: Int) = route.replace("{id}", id.toString())
    }
    data object EditPasswordItem : Routes("edit-password-item/{id}") {
        fun getPath(id: Int) = route.replace("{id}", id.toString())
    }
    data object AddCategoryItem: Routes("add-category-item")
    data object CategoryItemDetail : Routes("category-item-detail/{id}") {
        fun getPath(id: Int) = route.replace("{id}", id.toString())
    }
}
