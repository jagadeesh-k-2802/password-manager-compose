package com.jackappsdev.password_manager.presentation.components

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
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.Dispatchers

/**
 * Common search bar used across multiple screens (e.g. Home, Autofill).
 *
 * - Handles layout, icons, paddings and insets.
 * - Optionally triggers a debounced search callback after text changes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (() -> Unit)? = null,
    onClear: () -> Unit,
    enableDebounce: Boolean = false,
    onDebouncedSearch: (() -> Unit)? = null,
) {
    val debouncedSearch = remember(enableDebounce, onDebouncedSearch) {
        if (enableDebounce && onDebouncedSearch != null) {
            debounce<Unit>(400, Dispatchers.IO) {
                onDebouncedSearch()
            }
        } else {
            null
        }
    }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { value ->
                    onQueryChange(value)
                    debouncedSearch?.invoke(Unit)
                },
                onSearch = { onSearch?.invoke() },
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
                    if (query.isNotEmpty()) {
                        IconButton(onClick = onClear) {
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = pagePadding),
        windowInsets = windowInsetsVerticalZero,
    ) {}

    Spacer(modifier = Modifier.height(8.dp))
}
