package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.SortByAlpha
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
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortModalSheet(sheetState: SheetState, onValueSet: (SortBy) -> Unit) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState
    ) {
        Text(
            "Sort By",
            modifier = Modifier.padding(pagePadding),
            style = MaterialTheme.typography.titleLarge
        )

        ListItem(
            headlineContent = { Text("A -> Z") },
            modifier = Modifier.clickable { onValueSet(SortBy.ALPHABET_ASCENDING) },
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.SortByAlpha,
                    contentDescription = "Sort by ascending alphabet"
                )
            }
        )

        ListItem(
            headlineContent = { Text("Z -> A") },
            modifier = Modifier.clickable { onValueSet(SortBy.ALPHABET_DESCENDING) },
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.SortByAlpha,
                    contentDescription = "Sort by descending alphabet"
                )
            }
        )

        ListItem(
            headlineContent = { Text("Newest -> Oldest") },
            modifier = Modifier.clickable { onValueSet(SortBy.NEWEST) },
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = "Sort by newest"
                )
            }
        )

        ListItem(
            headlineContent = { Text("Oldest -> Newest") },
            modifier = Modifier.clickable { onValueSet(SortBy.OLDEST) },
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = "Sort by oldest"
                )
            }
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}
