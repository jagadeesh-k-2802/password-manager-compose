package com.jackappsdev.password_manager.presentation.screens.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_BIOMETRIC_ENROLL
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.navigation.navigate

@SuppressLint("QueryPermissionsNeeded")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state
    var isImportPasswordsDialogVisible by rememberSaveable { mutableStateOf(false) }
    var importFileUri by remember { mutableStateOf<Uri?>(null) }
    var isInvalidPassword by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val isScreenLockAvailable = remember {
        val manager = BiometricManager.from(context)
        val credentials = BIOMETRIC_WEAK or BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        manager.canAuthenticate(credentials) == BiometricManager.BIOMETRIC_SUCCESS
    }

    val importIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            importFileUri = uri
            isImportPasswordsDialogVisible = true
        }
    }

    val exportIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data

            if (uri != null) viewModel.exportData(uri) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_passwords_exported),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    if (isImportPasswordsDialogVisible) ImportPasswordsDialog(
        isInvalidPassword = isInvalidPassword,
        onConfirm = { password ->
            if (importFileUri != null) viewModel.importData(importFileUri!!, password) { isDone ->
                if (!isDone) isInvalidPassword = true
            }
        },
        onDismiss = {
            isImportPasswordsDialogVisible = false
            isInvalidPassword = false
        }
    )

    val onNoLockScreen = remember {
        {
            Toast
                .makeText(
                    context,
                    context.getString(R.string.toast_setup_lock_screen),
                    Toast.LENGTH_SHORT
                )
                .show()

            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent(ACTION_BIOMETRIC_ENROLL)
            } else {
                Intent(ACTION_SECURITY_SETTINGS)
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_settings)) }) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Lock, null) },
                trailingContent = { Icon(Icons.Outlined.ChevronRight, null) },
                headlineContent = { Text(stringResource(R.string.label_change_lock_password)) },
                modifier = Modifier.clickable { navController.navigate(Routes.ChangePassword) }
            )

            ListItem(
                leadingContent = { Icon(Icons.Outlined.Category, null) },
                trailingContent = { Icon(Icons.Outlined.ChevronRight, null) },
                headlineContent = { Text(stringResource(R.string.label_manage_categories)) },
                modifier = Modifier.clickable { navController.navigate(Routes.ManageCategories) }
            )

            ListItem(
                leadingContent = { Icon(Icons.Outlined.Watch, null) },
                headlineContent = { Text(stringResource(R.string.label_android_watch)) },
                trailingContent = { Icon(Icons.Outlined.ChevronRight, null) },
                modifier = Modifier.clickable { navController.navigate(Routes.AndroidWatch) }
            )

            ListItem(
                leadingContent = { Icon(Icons.Outlined.LockOpen, null) },
                headlineContent = { Text(stringResource(R.string.label_use_screen_lock_to_unlock)) },
                trailingContent = {
                    Switch(
                        checked = state.useScreenLockToUnlock == true,
                        onCheckedChange = { value ->
                            if (isScreenLockAvailable) {
                                viewModel.setBiometricUnlock(value)
                            } else {
                                onNoLockScreen()
                            }
                        }
                    )
                },
                modifier = Modifier
                    .clickable {
                        if (isScreenLockAvailable) {
                            viewModel.setBiometricUnlock(state.useScreenLockToUnlock != true)
                        } else {
                            onNoLockScreen()
                        }
                    }
                    .alpha(if (isScreenLockAvailable) 1f else 0.5f)
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ListItem(
                leadingContent = { Icon(Icons.Outlined.Palette, null) },
                headlineContent = { Text(stringResource(R.string.label_dynamic_colors)) },
                trailingContent = {
                    Switch(
                        checked = state.useDynamicColors == true,
                        onCheckedChange = { value -> viewModel.setDynamicColors(value) }
                    )
                },
                modifier = Modifier
                    .clickable {
                        viewModel.setDynamicColors(state.useDynamicColors != true)
                    }
            )

            ListItem(
                leadingContent = { Icon(Icons.Outlined.Download, null) },
                headlineContent = { Text(stringResource(R.string.label_import_passwords)) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.setType("application/*")
                    importIntent.launch(intent)
                }
            )

            ListItem(
                leadingContent = { Icon(Icons.Outlined.Upload, null) },
                headlineContent = { Text(stringResource(R.string.label_export_passwords)) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.setType("application/vnd.sqlite3")
                    intent.putExtra(Intent.EXTRA_TITLE, "passwords.db")
                    exportIntent.launch(intent)
                }
            )

            ListItem(
                leadingContent = { Icon(Icons.Outlined.Numbers, null) },
                headlineContent = { Text(stringResource(R.string.label_app_version)) },
                modifier = Modifier.clickable {}
            )
        }
    }
}
