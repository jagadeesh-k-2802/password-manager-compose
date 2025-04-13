package com.jackappsdev.password_manager.presentation.screens.manage_categories.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesUiEvent

@Composable
fun CategoryItemsView(
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    categoryItems: State<List<CategoryModel>>?,
    onEvent: (ManageCategoriesUiEvent) -> Unit
) {
    LazyColumn(
        state = lazyColumnState,
        modifier = modifier
    ) {
        categoryItems?.let {
            items(it.value) { item ->
                CategoryItem(
                    item = item,
                    onClick = { onEvent(ManageCategoriesUiEvent.NavigateToCategoryItem(item.id ?: 0)) }
                )
            }
        }
    }
}
