package com.jagadeesh.passwordmanager.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jagadeesh.passwordmanager.core.debounce
import com.jagadeesh.passwordmanager.presentation.navigation.Routes
import com.jagadeesh.passwordmanager.presentation.navigation.navigate
import com.jagadeesh.passwordmanager.presentation.theme.pagePadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val filterBySheetState = rememberModalBottomSheetState()
    val sortBySheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearching by rememberSaveable { mutableStateOf(false) }
    val passwordItems = state.items?.collectAsState()
    val filteredItems = state.filteredItems?.collectAsState()
    val categoryItems by viewModel.categoryItems.collectAsState(initial = listOf())

    val debouncedFilter = remember {
        debounce<Unit>(1000, Dispatchers.IO) { viewModel.searchItems(searchQuery) }
    }

    if (sortBySheetState.isVisible) SortModalSheet(sortBySheetState) { sortBy ->
        viewModel.setSortBy(sortBy)
        scope.launch { sortBySheetState.hide() }
    }

    if (filterBySheetState.isVisible) FilterByCategoryModalSheet(
        filterBySheetState,
        categoryItems
    ) { filterBy ->
        viewModel.filterByCategory(filterBy)
        scope.launch { filterBySheetState.hide() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Passwords") },
                actions = {
                    IconButton(onClick = { scope.launch { filterBySheetState.show() } }) {
                        Icon(Icons.Filled.FilterAlt, contentDescription = "Filter")
                    }

                    IconButton(onClick = { scope.launch { sortBySheetState.show() } }) {
                        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.AddPasswordItem) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new item")
            }
        }
    ) { contentPadding ->
        if (state.isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else if (passwordItems?.value?.isEmpty() == true) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Icon(
                    Icons.Outlined.Info,
                    "No passwords",
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("No Passwords Saved")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                item {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { query ->
                            searchQuery = query
                            isSearching = true
                            debouncedFilter(Unit)
                        },
                        onSearch = { keyboardController?.hide() },
                        active = false,
                        onActiveChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = pagePadding),
                        placeholder = { Text("Search") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    isSearching = true
                                    keyboardController?.hide()
                                    viewModel.searchItems(searchQuery)
                                }) {
                                    Icon(Icons.Filled.Clear, "Clear search")
                                }
                            }
                        }
                    ) {}

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (searchQuery.isNotEmpty() && filteredItems?.value?.isEmpty() == true) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize()
                        ) {
                            Icon(
                                Icons.Outlined.Info,
                                "No items",
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No Items Found")
                        }
                    }
                }

                if (filteredItems != null) {
                    items(filteredItems.value) { item ->
                        PasswordItem(item) {
                            navController.navigate(Routes.PasswordItemDetail.getPath(item.id ?: 0))
                        }
                    }
                } else {
                    passwordItems?.let {
                        items(it.value) { item ->
                            PasswordItem(item) {
                                navController.navigate(Routes.PasswordItemDetail.getPath(item.id ?: 0))
                            }
                        }
                    }
                }
            }
        }
    }
}
