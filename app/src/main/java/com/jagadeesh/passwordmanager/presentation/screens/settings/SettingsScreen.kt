package com.jagadeesh.passwordmanager.presentation.screens.settings

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    val isScreenLockAvailable = remember {
        val manager = BiometricManager.from(context)
        val credentials = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        manager.canAuthenticate(credentials) == BiometricManager.BIOMETRIC_SUCCESS
    }

    val importIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            // TODO: Show dialog about data loss and new master password is db's password
            if (uri != null) viewModel.importData(uri)
        }
    }

    val exportIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) viewModel.exportData(uri) {
                Toast.makeText(context, "Database Exported Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                trailingContent = { Icon(Icons.Filled.ChevronRight, null) },
                headlineContent = { Text("Change Lock Password") },
                modifier = Modifier.clickable { navController.navigate(Routes.ChangePassword) }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Category, null) },
                trailingContent = { Icon(Icons.Filled.ChevronRight, null) },
                headlineContent = { Text("Manage Categories") },
                modifier = Modifier.clickable { navController.navigate(Routes.ManageCategories) }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.LockOpen, null) },
                headlineContent = { Text("Use Screen Lock to Unlock") },
                trailingContent = {
                    Switch(
                        checked = state.useScreenLockToUnlock == true,
                        onCheckedChange = { value -> viewModel.setBiometricUnlock(value) }
                    )
                },
                modifier = Modifier
                    .clickable {
                        if (isScreenLockAvailable) {
                            viewModel.setBiometricUnlock(state.useScreenLockToUnlock != true)
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Please, Setup your device lock screen first.",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                    .alpha(if (isScreenLockAvailable) 1f else 0.5f)
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Download, null) },
                headlineContent = { Text("Import Data") },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.setType("application/*")
                    importIntent.launch(intent)
                }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Upload, null) },
                headlineContent = { Text("Export Data") },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.setType("application/vnd.sqlite3")
                    intent.putExtra(Intent.EXTRA_TITLE, "passwords.db")
                    exportIntent.launch(intent)
                }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Numbers, null) },
                headlineContent = { Text("App Version: 1.0.0") },
                modifier = Modifier.clickable {}
            )
        }
    }
}
