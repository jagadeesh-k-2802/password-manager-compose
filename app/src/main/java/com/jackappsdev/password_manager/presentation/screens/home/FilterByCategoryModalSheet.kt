package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterByCategoryModalSheet(
    sheetState: SheetState,
    categoryItems: List<CategoryModel>,
    onValueChoose: (FilterBy) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState
    ) {
        Text(
            "Filter By Category",
            modifier = Modifier.padding(pagePadding),
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn {
            item {
                ListItem(
                    leadingContent = { Icon(Icons.Filled.SelectAll, "All Items") },
                    headlineContent = { Text("All") },
                    modifier = Modifier.clickable { onValueChoose(FilterBy.All) }
                )
            }

            item {
                ListItem(
                    leadingContent = { Icon(Icons.Filled.Block, "No Category Items") },
                    headlineContent = { Text("No Category Items") },
                    modifier = Modifier.clickable { onValueChoose(FilterBy.NoCategoryItems) }
                )
            }

            items(categoryItems) { item ->
                ListItem(
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(parseColor(item.color))
                                .size(24.dp)
                        ) {}
                    },
                    headlineContent = { Text(item.name) },
                    modifier = Modifier.clickable { onValueChoose(FilterBy.Category(item.id ?: 0)) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
