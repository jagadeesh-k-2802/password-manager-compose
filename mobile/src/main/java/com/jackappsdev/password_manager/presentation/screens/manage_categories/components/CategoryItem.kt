package com.jackappsdev.password_manager.presentation.screens.manage_categories.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.components.ColoredCircle

@Composable
fun CategoryItem(item: CategoryModel, onClick: () -> Unit) {
    ListItem(
        leadingContent = { ColoredCircle(color = item.color) },
        headlineContent = { Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
