package com.jagadeesh.passwordmanager.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jagadeesh.passwordmanager.presentation.navigation.Routes
import com.jagadeesh.passwordmanager.presentation.navigation.navigateWithState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var bottomBarVisible by rememberSaveable { mutableStateOf(true) }
    val currentRoute = navBackStackEntry?.destination?.route

    bottomBarVisible = when (currentRoute) {
        Routes.Home.route -> true
        Routes.PasswordGenerator.route -> true
        Routes.Settings.route -> true
        else -> false
    }

    if (bottomBarVisible) {
        NavigationBar {
            NavigationBarItem(
                selected = currentRoute?.equals(Routes.Home.route) == true,
                onClick = { navController.navigateWithState(Routes.Home) },
                label = { Text("Home") },
                icon = {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Home Screen"
                    )
                },
            )

            NavigationBarItem(
                selected = currentRoute?.equals(Routes.PasswordGenerator.route) == true,
                onClick = { navController.navigateWithState(Routes.PasswordGenerator) },
                label = { Text("Password Generator") },
                icon = {
                    Icon(
                        Icons.Filled.Security,
                        contentDescription = "Password Generator Screen"
                    )
                }
            )

            NavigationBarItem(
                selected = currentRoute?.equals(Routes.Settings.route) == true,
                onClick = { navController.navigateWithState(Routes.Settings) },
                label = { Text("Settings") },
                icon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings Screen"
                    )
                }
            )
        }
    }
}
