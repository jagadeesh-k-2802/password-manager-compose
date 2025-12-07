package com.jackappsdev.password_manager.autofill.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.autofill.AutofillState
import com.jackappsdev.password_manager.autofill.AutofillUiEvent
import com.jackappsdev.password_manager.presentation.components.CommonSearchBar
import com.jackappsdev.password_manager.presentation.components.EmptyStateView
import com.jackappsdev.password_manager.presentation.screens.home.components.PasswordItem

@Composable
fun AutofillSelectionList(
    modifier: Modifier = Modifier,
    state: AutofillState,
    onEvent: (AutofillUiEvent) -> Unit
) {
    val filteredItems = state.filteredItems?.collectAsState()?.value
    val passwordItems = state.items?.collectAsState()?.value
    val items = filteredItems ?: passwordItems

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            CommonSearchBar(
                query = state.searchQuery,
                onQueryChange = { query -> onEvent(AutofillUiEvent.EnterSearchQuery(query)) },
                onClear = { onEvent(AutofillUiEvent.ClearSearch) }
            )
        }

        if (state.searchQuery.isNotEmpty() && filteredItems?.isEmpty() == true) {
            item {
                EmptyStateView(
                    icon = R.drawable.search_empty_state,
                    title = R.string.text_no_matching_passwords_found,
                )
            }
        }

        items?.let {
            items(items) { item ->
                PasswordItem(
                    item = item,
                    onClick = { onEvent(AutofillUiEvent.ItemSelected(item)) }
                )
            }
        }
    }
}
