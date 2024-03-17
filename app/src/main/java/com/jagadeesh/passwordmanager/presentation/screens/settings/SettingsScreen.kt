package com.jagadeesh.passwordmanager.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Settings") }) }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            /**
             * TODO:
             * 1. Import/Export DB
             * 2. Enable/Disable Biometric Authentication
             * 2. Dark Theme Toggle
             * 3. About Info
             */
        }
    }
}
