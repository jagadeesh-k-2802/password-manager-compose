package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jackappsdev.password_manager.domain.model.PasswordItemModel

@Composable
fun PasswordItem(item: PasswordItemModel, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.name) },
        supportingContent = { Text(item.username) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
