package com.jackappsdev.password_manager.presentation.screens.manage_categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.domain.model.CategoryModel

@Composable
fun CategoryItem(item: CategoryModel, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(parseColor(item.color))
                    .size(24.dp)
            ) {}
        },
        headlineContent = { Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
