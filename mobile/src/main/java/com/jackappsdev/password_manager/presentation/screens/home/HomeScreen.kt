package com.jackappsdev.password_manager.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FilterAltOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.debounce
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.home.composables.FilterByCategoryModalSheet
import com.jackappsdev.password_manager.presentation.screens.home.composables.PasswordItem
import com.jackappsdev.password_manager.presentation.screens.home.composables.SortModalSheet
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowinsetsVerticalZero
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
    val context = LocalContext.current
    val state = viewModel.state
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearching by rememberSaveable { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val passwordItems = state.items?.collectAsState()?.value
    val filteredItems = state.filteredItems?.collectAsState()?.value
    val categoryItems by viewModel.categoryItems.collectAsState(initial = listOf())
    val focusManager = LocalFocusManager.current

    val debouncedFilter = remember {
        debounce<Unit>(400, Dispatchers.IO) { viewModel.searchItems(searchQuery) }
    }

    if (sortBySheetState.isVisible) SortModalSheet(
        sheetState = sortBySheetState,
        currentSortBy = state.sortBy
    ) { sortBy ->
        scope.launch {
            if (categoryItems.isNotEmpty()) lazyColumnState.animateScrollToItem(0)
            sortBySheetState.hide()
        }

        viewModel.setSortBy(sortBy, searchQuery)
    }

    if (filterBySheetState.isVisible) FilterByCategoryModalSheet(
        sheetState = filterBySheetState,
        currentFilterBy = state.filterBy,
        categoryItems = categoryItems
    ) { filterBy ->
        scope.launch {
            if (categoryItems.isNotEmpty()) lazyColumnState.animateScrollToItem(0)
            filterBySheetState.hide()
        }

        viewModel.filterByCategory(filterBy, searchQuery)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_app_locked), Toast.LENGTH_SHORT
                        ).show()

                        viewModel.lockApplication()
                    }) {
                        Icon(
                            Icons.Outlined.Lock,
                            stringResource(R.string.accessibility_lock_application)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_passwords)) },
                actions = {
                    IconButton(onClick = { scope.launch { filterBySheetState.show() } }) {
                        Icon(
                            if (state.filterBy == FilterBy.All) Icons.Outlined.FilterAltOff
                            else Icons.Outlined.FilterAlt,
                            contentDescription = stringResource(R.string.accessibility_filter)
                        )
                    }

                    IconButton(onClick = { scope.launch { sortBySheetState.show() } }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Sort,
                            contentDescription = stringResource(R.string.accessibility_sort)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = windowinsetsVerticalZero,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        scope.launch { lazyColumnState.animateScrollToItem(0) }
                    }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.AddPasswordItem) }) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = stringResource(R.string.accessibility_add_item)
                )
            }
        },
        modifier = Modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        )
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
        } else if (passwordItems?.isEmpty() == true) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(R.drawable.task_empty_state),
                    contentDescription = stringResource(R.string.accessibility_no_passwords),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.text_no_passwords))
            }
        } else {
            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                item {
                    SearchBar(
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = searchQuery,
                                onQueryChange = { query ->
                                    searchQuery = query
                                    isSearching = true
                                    debouncedFilter(Unit)
                                },
                                onSearch = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                },
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
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = {
                                            searchQuery = ""
                                            isSearching = true
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                            viewModel.searchItems(searchQuery)
                                        }) {
                                            Icon(
                                                Icons.Outlined.Clear,
                                                stringResource(R.string.accessibility_clear_search)
                                            )
                                        }
                                    }
                                },
                                interactionSource = null,
                            )
                        },
                        expanded = false,
                        onExpandedChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = pagePadding),
                        windowInsets = windowinsetsVerticalZero,
                    ) {}

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (searchQuery.isNotEmpty() && filteredItems?.isEmpty() == true) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize()
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.search_empty_state),
                                contentDescription = stringResource(R.string.accessibility_no_items),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(180.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(stringResource(R.string.text_no_matching_passwords_found))
                        }
                    }
                }

                if (filteredItems != null) {
                    items(filteredItems) { item ->
                        PasswordItem(item) {
                            navController.navigate(Routes.PasswordItemDetail(item.id ?: 0))
                        }
                    }
                } else {
                    passwordItems?.let {
                        items(it) { item ->
                            PasswordItem(item) {
                                navController.navigate(
                                    Routes.PasswordItemDetail(item.id ?: 0)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
