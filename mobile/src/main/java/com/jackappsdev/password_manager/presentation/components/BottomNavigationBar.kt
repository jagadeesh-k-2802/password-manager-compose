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
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.navigation.NavigationState
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes

@Composable
fun BottomNavigationBar(navigator: Navigator, navigationState: NavigationState) {
    val currentRoute = navigationState.currentRoute

    val bottomBarVisible = when (currentRoute) {
        Routes.Home,
        Routes.PasswordGenerator,
        Routes.Settings -> true
        else -> false
    }

    if (bottomBarVisible) {
        NavigationBar {
            NavigationBarItem(
                selected = currentRoute == Routes.Home,
                onClick = { navigator.navigate(Routes.Home) },
                label = { Text(stringResource(R.string.nav_home)) },
                icon = {
                    if (currentRoute == Routes.Home) {
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
                selected = currentRoute == Routes.PasswordGenerator,
                onClick = { navigator.navigate(Routes.PasswordGenerator) },
                label = { Text(stringResource(R.string.nav_generator)) },
                icon = {
                    if (currentRoute == Routes.PasswordGenerator) {
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
                selected = currentRoute == Routes.Settings,
                onClick = { navigator.navigate(Routes.Settings) },
                label = { Text(stringResource(R.string.nav_settings)) },
                icon = {
                    if (currentRoute == Routes.Settings) {
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
