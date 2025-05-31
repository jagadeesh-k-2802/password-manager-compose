package com.jackappsdev.password_manager.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Graph {
    @Serializable
    data object LockGraph : Graph

    @Serializable
    data object UnlockedGraph : Graph
}

sealed interface Routes {
    @Serializable
    data object PasswordLock : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object AddPasswordItem : Routes

    @Serializable
    data object PasswordGenerator : Routes

    @Serializable
    data object Settings : Routes

    @Serializable
    data object ChangePassword : Routes

    @Serializable
    data object AndroidWatch : Routes

    @Serializable
    data object Pin : Routes

    @Serializable
    data object ManageCategories : Routes

    @Serializable
    data class PasswordItemDetail(val id: Int) : Routes

    @Serializable
    data class EditPasswordItem(val id: Int) : Routes

    @Serializable
    data object AddCategoryItem : Routes

    @Serializable
    data class CategoryItemDetail(val id: Int) : Routes
}
