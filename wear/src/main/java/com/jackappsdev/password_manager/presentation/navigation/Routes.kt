package com.jackappsdev.password_manager.presentation.navigation

sealed class Graph(val route: String) {
    data object LockGraph : Graph("lock")
    data object UnlockedGraph : Graph("unlocked")
}

sealed class Routes(val route: String) {
    data object PasswordLock : Routes("password-lock")
    data object Home : Routes("home")
    data object PasswordItemDetail : Routes("password-item-detail/{id}") {
        operator fun invoke(id: Int) = route.replace("{id}", id.toString())
    }
}
