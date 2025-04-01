package com.jackappsdev.password_manager.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun ToggleSettingItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    leadingIcon: ImageVector? = null,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = leadingIcon?.let { { Icon(it, null) } },
        headlineContent = { Text(title) },
        supportingContent = { subtitle?.let { Text(it) } },
        trailingContent = { Switch(checked = checked, onCheckedChange = { onClick.invoke() }) },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun ToggleSettingItemPreview() {
    PasswordManagerTheme {
        ToggleSettingItem(
            checked = true,
            leadingIcon = Icons.Outlined.LockOpen,
            title = stringResource(R.string.label_use_screen_lock_to_unlock),
            onClick = {}
        )
    }
}
