package com.jackappsdev.password_manager.presentation.screens.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsState
import com.jackappsdev.password_manager.presentation.screens.settings.event.SettingsUiEvent

@Composable
fun UpdateSettingItem(
    modifier: Modifier = Modifier,
    state: SettingsState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    when {
        state.isAppUpdateDownloaded == true -> {
            SettingItem(
                modifier = modifier,
                leadingIcon = Icons.Outlined.DownloadDone,
                trailingIcon = Icons.Outlined.ChevronRight,
                title = stringResource(R.string.app_update_downloaded),
                onClick = { onEvent(SettingsUiEvent.CompleteAppUpdate) }
            )
        }

        state.isAppUpdateDownloading == true -> {
            SettingItem(
                modifier = modifier,
                leadingIcon = Icons.Outlined.Update,
                title = stringResource(R.string.app_update_downloading),
                onClick = { }
            )
        }

        state.isAppUpdateAvailable == true -> {
            SettingItem(
                modifier = modifier,
                leadingIcon = Icons.Outlined.Update,
                trailingIcon = Icons.Outlined.ChevronRight,
                title = stringResource(R.string.app_update_available),
                onClick = { onEvent(SettingsUiEvent.StartAppUpdate) }
            )
        }
    }
}
