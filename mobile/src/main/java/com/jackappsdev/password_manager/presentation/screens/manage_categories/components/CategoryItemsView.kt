package com.jackappsdev.password_manager.presentation.screens.manage_categories.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.navigation.Routes

@Composable
fun CategoryItemsView(
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    navController: NavController,
    categoryItems: State<List<CategoryModel>>?
) {
    LazyColumn(
        state = lazyColumnState,
        modifier = modifier
    ) {
        categoryItems?.let {
            items(it.value) { item ->
                CategoryItem(item) {
                    navController.navigate(Routes.CategoryItemDetail(item.id ?: 0))
                }
            }
        }
    }
}
