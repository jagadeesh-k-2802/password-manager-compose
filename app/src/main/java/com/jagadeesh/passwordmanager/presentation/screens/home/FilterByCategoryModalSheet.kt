package com.jagadeesh.passwordmanager.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jagadeesh.passwordmanager.presentation.theme.pagePadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterByCategoryModalSheet(
    sheetState: SheetState,
    viewModel: HomeViewModel,
    onValueChoose: (Int?) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState
    ) {
        val categoryItems by viewModel.categoryItems.collectAsState(initial = listOf())

        Text(
            "Filter By Category",
            modifier = Modifier.padding(pagePadding),
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn {
            item {
                ListItem(
                    headlineContent = { Text("All") },
                    modifier = Modifier.clickable { onValueChoose(null) }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("No Category Items") },
                    modifier = Modifier.clickable { onValueChoose(null) }
                )
            }

            items(categoryItems) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    modifier = Modifier.clickable { onValueChoose(item.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
