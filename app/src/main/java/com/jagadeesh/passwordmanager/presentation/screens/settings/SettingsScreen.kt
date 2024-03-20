package com.jagadeesh.passwordmanager.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jagadeesh.passwordmanager.presentation.navigation.Routes
import com.jagadeesh.passwordmanager.presentation.navigation.navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Settings") }) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .scrollable(scrollState, Orientation.Vertical)
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Filled.Lock, null) },
                trailingContent = {Icon(Icons.Filled.ChevronRight, null)},
                headlineContent = { Text("Change Lock Password") },
                modifier = Modifier.clickable {
                    navController.navigate(Routes.ChangePassword)
                }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Category, null) },
                trailingContent = {Icon(Icons.Filled.ChevronRight, null)},
                headlineContent = { Text("Manage Categories") },
                modifier = Modifier.clickable {/* TODO: Manage Categories */ }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Fingerprint, null) },
                headlineContent = { Text("Use Biometric Unlock") },
                trailingContent = {
                    Switch(
                        checked = state.useBiometricUnlock == true,
                        onCheckedChange = { value -> viewModel.setBiometricUnlock(value) }
                    )
                },
                modifier = Modifier.clickable {
                    viewModel.setBiometricUnlock(state.useBiometricUnlock != true)
                }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Download, null) },
                headlineContent = { Text("Import Data") },
                modifier = Modifier.clickable {/* TODO: Import Data */ }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Upload, null) },
                headlineContent = { Text("Export Data") },
                modifier = Modifier.clickable {/* TODO: Export Data */ }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Numbers, null) },
                headlineContent = { Text("App Version: 1.0.0") },
                modifier = Modifier.clickable {}
            )
        }
    }
}
