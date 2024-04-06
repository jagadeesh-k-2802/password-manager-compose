package com.jackappsdev.password_manager.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material.icons.sharp.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.navigation.navigateWithState

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
                    if (currentRoute?.equals(Routes.Home.route) == true) {
                        Icon(
                            Icons.Sharp.Home,
                            contentDescription = "Home Screen"
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Home Screen"
                        )
                    }
                },
            )

            NavigationBarItem(
                selected = currentRoute?.equals(Routes.PasswordGenerator.route) == true,
                onClick = { navController.navigateWithState(Routes.PasswordGenerator) },
                label = { Text("Generator", softWrap = true, textAlign = TextAlign.Center) },
                icon = {
                    if (currentRoute?.equals(Routes.PasswordGenerator.route) == true) {
                        Icon(
                            Icons.Sharp.VpnKey,
                            contentDescription = "Password Generator Screen"
                        )
                    } else {
                        Icon(
                            Icons.Outlined.VpnKey,
                            contentDescription = "Password Generator Screen"
                        )
                    }

                }
            )

            NavigationBarItem(
                selected = currentRoute?.equals(Routes.Settings.route) == true,
                onClick = { navController.navigateWithState(Routes.Settings) },
                label = { Text("Settings") },
                icon = {
                    if (currentRoute?.equals(Routes.Settings.route) == true) {
                        Icon(
                            Icons.Sharp.Settings,
                            contentDescription = "Settings Screen"
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings Screen"
                        )
                    }
                }
            )
        }
    }
}
