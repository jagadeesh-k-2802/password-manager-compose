package com.jackappsdev.password_manager.presentation.screens.settings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    title: String,
    onClick: () -> Unit = {}
) {
    ListItem(
        leadingContent = {
            Icon(leadingIcon, contentDescription = null)
        },
        trailingContent = trailingIcon?.let {
            {
                AnimatedContent(trailingIcon) {
                    Icon(imageVector = it, contentDescription = null)
                }
            }
        },
        headlineContent = {
            Text(title)
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun SettingItemPreview() {
    PasswordManagerTheme {
        SettingItem(
            leadingIcon = Icons.Outlined.Lock,
            trailingIcon = Icons.Outlined.ChevronRight,
            title = stringResource(R.string.label_change_lock_password)
        )
    }
}
