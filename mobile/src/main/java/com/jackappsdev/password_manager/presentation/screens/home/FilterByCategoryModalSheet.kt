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
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterByCategoryModalSheet(
    sheetState: SheetState,
    currentFilterBy: FilterBy,
    categoryItems: List<CategoryModel>,
    onValueChoose: (FilterBy) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Text(
            stringResource(R.string.text_filter_by_category),
            modifier = Modifier.padding(pagePadding),
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn {
            item {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Outlined.SelectAll,
                            stringResource(R.string.accessibility_all_items)
                        )
                    },
                    headlineContent = {
                        Text(stringResource(R.string.label_all))
                    },
                    trailingContent = {
                        RadioButton(
                            selected = currentFilterBy == FilterBy.All,
                            onClick = { onValueChoose(FilterBy.All) }
                        )
                    },
                    modifier = Modifier.clickable { onValueChoose(FilterBy.All) }
                )
            }

            item {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Block,
                            stringResource(R.string.accessibility_no_category_items)
                        )
                    },
                    headlineContent = {
                        Text(stringResource(R.string.label_no_category_items))
                    },
                    trailingContent = {
                        RadioButton(
                            selected = currentFilterBy == FilterBy.NoCategoryItems,
                            onClick = { onValueChoose(FilterBy.NoCategoryItems) }
                        )
                    },
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
                    headlineContent = {
                        Text(item.name)
                    },
                    trailingContent = {
                        RadioButton(
                            selected = currentFilterBy == item.id?.let { FilterBy.Category(it) },
                            onClick = { onValueChoose(FilterBy.Category(item.id ?: 0)) }
                        )
                    },
                    modifier = Modifier.clickable {
                        onValueChoose(FilterBy.Category(item.id ?: 0))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
