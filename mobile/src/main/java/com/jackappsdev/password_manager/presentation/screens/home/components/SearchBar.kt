package com.jackappsdev.password_manager.presentation.screens.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.debounce
import com.jackappsdev.password_manager.presentation.screens.home.HomeState
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(state: HomeState, onEvent: (HomeUiEvent) -> Unit) {
    val debouncedFilter = remember {
        debounce<Unit>(400, Dispatchers.IO) {
            onEvent(HomeUiEvent.SearchItems)
        }
    }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = state.searchQuery,
                onQueryChange = { query ->
                    onEvent(HomeUiEvent.OnEnterSearchQuery(query))
                    debouncedFilter(Unit)
                },
                onSearch = { onEvent(HomeUiEvent.OnSearch) },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text(stringResource(R.string.label_search)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.accessibility_search)
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onEvent(HomeUiEvent.OnClearSearch) }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = stringResource(R.string.accessibility_clear_search)
                            )
                        }
                    }
                },
                interactionSource = null
            )
        },
        expanded = false,
        onExpandedChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = pagePadding),
        windowInsets = windowInsetsVerticalZero,
    ) {}

    Spacer(modifier = Modifier.height(8.dp))
}
