package com.jackappsdev.password_manager.presentation.screens.settings

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.TableRows
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.BuildConfig
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.isAtLeastAndroid
import com.jackappsdev.password_manager.presentation.components.PasswordInputDialog
import com.jackappsdev.password_manager.presentation.screens.settings.components.SettingItem
import com.jackappsdev.password_manager.presentation.components.ToggleSettingItem
import com.jackappsdev.password_manager.presentation.screens.settings.components.UpdateSettingItem
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsEffectHandler
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsUiEffect
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsUiEvent
import com.jackappsdev.password_manager.presentation.screens.settings.model.ExportPasswordAuthType.PasswordAuth
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    effectFlow: Flow<SettingsUiEffect>,
    effectHandler: SettingsEffectHandler,
    onEvent: (SettingsUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    val importIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onEvent(SettingsUiEvent.SavePasswordsUri(result.data?.data.toString()))
        }
    }

    val exportIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onEvent(SettingsUiEvent.ExportPasswords(result.data?.data.toString()))
        }
    }

    val exportCsvIntent = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onEvent(SettingsUiEvent.ExportPasswordsCsv(result.data?.data.toString()))
        }
    }

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is SettingsUiEffect.StartAppUpdate -> onStartAppUpdate(effect.appUpdateManager)
                    is SettingsUiEffect.OpenImportPasswordsIntent -> onOpenImportPasswordsIntent(importIntent)
                    is SettingsUiEffect.OpenExportPasswordsIntent -> onOpenExportPasswordsIntent(exportIntent)
                    is SettingsUiEffect.OpenExportCsvIntent -> onOpenExportCsvIntent(exportCsvIntent)
                    is SettingsUiEffect.BiometricAuthForExportPasswords -> onBiometricAuthForExportPasswords()
                    is SettingsUiEffect.BiometricAuthForExportCsv -> onBiometricAuthForExportCsv()
                    is SettingsUiEffect.PasswordsExported -> onPasswordsExported()
                    is SettingsUiEffect.BiometricAuthForScreenLock -> onBiometricAuthForScreenLock()
                    is SettingsUiEffect.OpenScreenLockSettings -> onOpenScreenLockSettings()
                    is SettingsUiEffect.NavigateToChangePassword -> onNavigateToChangePassword()
                    is SettingsUiEffect.NavigateToManageCategories -> onNavigateToManageCategories()
                    is SettingsUiEffect.NavigateToAndroidWatch -> onNavigateToAndroidWatch()
                    is SettingsUiEffect.NavigateToPin -> onNavigateToPin()
                    is SettingsUiEffect.OpenPlayStorePage -> onOpenPlayStorePage()
                }
            }
        }
    }

    if (state.isImportPasswordsDialogVisible) {
        PasswordInputDialog(
            title = R.string.dialog_title_import_passwords,
            description = R.string.text_import_passwords_note,
            label = R.string.label_database_password,
            isInvalidPassword = state.isImportPasswordInvalid,
            onConfirm = { onEvent(SettingsUiEvent.ImportPasswords(it)) },
            onDismiss = { onEvent(SettingsUiEvent.HideImportPasswordsDialog) }
        )
    }

    if (state.isExportPasswordsDialogVisible) {
        PasswordInputDialog(
            title = R.string.dialog_title_export_passwords,
            description = R.string.text_export_passwords,
            label = R.string.label_password,
            isInvalidPassword = state.isExportPasswordsPasswordInvalid,
            onConfirm = { onEvent(SettingsUiEvent.OpenExportPasswordsIntent(PasswordAuth(it))) },
            onDismiss = { onEvent(SettingsUiEvent.HideExportPasswordsDialog) }
        )
    }

    if (state.isExportCsvDialogVisible) {
        PasswordInputDialog(
            title = R.string.dialog_title_export_passwords_as_csv,
            description = R.string.text_export_passwords_csv_note,
            label = R.string.label_password,
            isInvalidPassword = state.isExportCsvPasswordInvalid,
            onConfirm = { onEvent(SettingsUiEvent.OpenExportCsvIntent(PasswordAuth(it))) },
            onDismiss = { onEvent(SettingsUiEvent.HideExportCsvDialog) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_settings)) },
                windowInsets = windowInsetsVerticalZero
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            UpdateSettingItem(
                state = state,
                onEvent = onEvent,
            )

            SettingItem(
                leadingIcon = Icons.Outlined.Lock,
                trailingIcon = Icons.Outlined.ChevronRight,
                title = stringResource(R.string.label_change_lock_password),
                onClick = { onEvent(SettingsUiEvent.NavigateToChangePassword) }
            )

            SettingItem(
                leadingIcon = Icons.Outlined.Category,
                trailingIcon = Icons.Outlined.ChevronRight,
                title = stringResource(R.string.label_manage_categories),
                onClick = { onEvent(SettingsUiEvent.NavigateToManageCategories) }
            )

            if (state.isChromeOS.not()) {
                SettingItem(
                    leadingIcon = Icons.Outlined.Watch,
                    trailingIcon = Icons.Outlined.ChevronRight,
                    title = stringResource(R.string.label_android_watch),
                    onClick = { onEvent(SettingsUiEvent.NavigateToAndroidWatch) }
                )
            }

            SettingItem(
                leadingIcon = Icons.Outlined.Pin,
                trailingIcon = Icons.Outlined.ChevronRight,
                title = stringResource(R.string.label_manage_pin),
                onClick = { onEvent(SettingsUiEvent.NavigateToPin) }
            )

            ToggleSettingItem(
                modifier = Modifier.alpha(if (state.isScreenLockAvailable == true) 1f else 0.5f),
                checked = state.useScreenLockToUnlock == true,
                leadingIcon = Icons.Outlined.LockOpen,
                title = stringResource(R.string.label_use_screen_lock_to_unlock),
                onClick = { onEvent(SettingsUiEvent.CheckScreenLockAvailable) }
            )

            if (isAtLeastAndroid(Build.VERSION_CODES.S)) {
                ToggleSettingItem(
                    checked = state.useDynamicColors == true,
                    leadingIcon = Icons.Outlined.Palette,
                    title = stringResource(R.string.label_dynamic_colors),
                    onClick = { onEvent(SettingsUiEvent.ToggleDynamicColors) }
                )
            }

            SettingItem(
                leadingIcon = Icons.Outlined.Download,
                title = stringResource(R.string.label_import_passwords_database),
                onClick = { onEvent(SettingsUiEvent.OpenImportPasswordsIntent) }
            )

            SettingItem(
                leadingIcon = Icons.Outlined.Upload,
                title = stringResource(R.string.label_export_passwords_database),
                onClick = { onEvent(SettingsUiEvent.CheckExportPasswordsAuth) }
            )

            SettingItem(
                leadingIcon = Icons.Outlined.TableRows,
                title = stringResource(R.string.label_export_passwords_csv),
                onClick = { onEvent(SettingsUiEvent.CheckExportCsvAuth) }
            )

            SettingItem(
                leadingIcon = Icons.Outlined.StarOutline,
                title = stringResource(R.string.label_rate_app),
                onClick = { onEvent(SettingsUiEvent.OpenPlayStorePage) }
            )

            SettingItem(
                leadingIcon = Icons.Outlined.Numbers,
                title = stringResource(R.string.label_app_version, BuildConfig.VERSION_NAME)
            )
        }
    }
}
