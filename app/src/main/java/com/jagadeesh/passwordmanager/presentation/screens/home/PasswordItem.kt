package com.jagadeesh.passwordmanager.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jagadeesh.passwordmanager.domain.model.PasswordItemModel

@Composable
fun PasswordItem(item: PasswordItemModel, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.name) },
        supportingContent = { Text(item.username) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
