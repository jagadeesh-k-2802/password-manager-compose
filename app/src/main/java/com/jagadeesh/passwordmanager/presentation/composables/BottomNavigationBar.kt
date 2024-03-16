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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jagadeesh.passwordmanager.presentation.navigation.Routes

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            selected = currentRoute?.equals(Routes.Home.route) == true,
            onClick = { onNavigate(navController, Routes.Home) },
            label = { Text("Home") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home Screen") },
        )

        NavigationBarItem(
            selected = currentRoute?.equals(Routes.PasswordGenerator.route) == true,
            onClick = { onNavigate(navController, Routes.PasswordGenerator) },
            label = { Text("Password Generator") },
            icon = { Icon(Icons.Filled.Security, contentDescription = "Password Generator Screen") }
        )

        NavigationBarItem(
            selected = currentRoute?.equals(Routes.Settings.route) == true,
            onClick = { onNavigate(navController, Routes.Settings) },
            label = { Text("Settings") },
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings Screen") }
        )
    }
}

private fun onNavigate(navController: NavController, route: Routes) {
    navController.navigate(route.route) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
