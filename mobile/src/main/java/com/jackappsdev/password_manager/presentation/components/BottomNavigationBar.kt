package com.jackappsdev.password_manager.presentation.components

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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.navigation.navigateWithState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var bottomBarVisible by rememberSaveable { mutableStateOf(true) }
    val currentRoute = navBackStackEntry?.destination

    bottomBarVisible = when {
        (currentRoute?.hasRoute<Routes.Home>() == true) -> true
        (currentRoute?.hasRoute<Routes.PasswordGenerator>() == true) -> true
        (currentRoute?.hasRoute<Routes.Settings>() == true) -> true
        else -> false
    }

    if (bottomBarVisible) {
        NavigationBar {
            NavigationBarItem(
                selected = currentRoute?.hasRoute<Routes.Home>() == true,
                onClick = { navController.navigateWithState(Routes.Home) },
                label = { Text(stringResource(R.string.nav_home)) },
                icon = {
                    if (currentRoute?.hasRoute<Routes.Home>() == true) {
                        Icon(
                            imageVector = Icons.Sharp.Home,
                            contentDescription = stringResource(R.string.accessibility_home_screen)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = stringResource(R.string.accessibility_home_screen)
                        )
                    }
                },
            )

            NavigationBarItem(
                selected = currentRoute?.hasRoute<Routes.PasswordGenerator>() == true,
                onClick = { navController.navigateWithState(Routes.PasswordGenerator) },
                label = { Text(stringResource(R.string.nav_generator)) },
                icon = {
                    if (currentRoute?.hasRoute<Routes.PasswordGenerator>() == true) {
                        Icon(
                            imageVector = Icons.Sharp.VpnKey,
                            contentDescription = stringResource(R.string.accessibility_password_generator_screen)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.VpnKey,
                            contentDescription = stringResource(R.string.accessibility_password_generator_screen)
                        )
                    }

                }
            )

            NavigationBarItem(
                selected = currentRoute?.hasRoute<Routes.Settings>() == true,
                onClick = { navController.navigateWithState(Routes.Settings) },
                label = { Text(stringResource(R.string.nav_settings)) },
                icon = {
                    if (currentRoute?.hasRoute<Routes.Settings>() == true) {
                        Icon(
                            imageVector = Icons.Sharp.Settings,
                            contentDescription = stringResource(R.string.accessibility_settings_screen)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.accessibility_settings_screen)
                        )
                    }
                }
            )
        }
    }
}
