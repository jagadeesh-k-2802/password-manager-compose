package com.jackappsdev.password_manager.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object PasswordLock : NavKey

    @Serializable
    data object Home : NavKey

    @Serializable
    data object AddPasswordItem : NavKey

    @Serializable
    data object PasswordGenerator : NavKey

    @Serializable
    data object Settings : NavKey

    @Serializable
    data object ChangePassword : NavKey

    @Serializable
    data object AndroidWatch : NavKey

    @Serializable
    data object Pin : NavKey

    @Serializable
    data object ManageCategories : NavKey

    @Serializable
    data class PasswordItemDetail(val id: Int) : NavKey

    @Serializable
    data class EditPasswordItem(val id: Int) : NavKey

    @Serializable
    data object AddCategoryItem : NavKey

    @Serializable
    data class CategoryItemDetail(val id: Int) : NavKey
}

internal val LOCKED_TOP_ROUTES = setOf(
    Routes.PasswordLock
)

internal val UNLOCKED_TOP_ROUTES = setOf(
    Routes.Home,
    Routes.PasswordGenerator,
    Routes.Settings
)