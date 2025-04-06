package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FilterAltOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.FilterBy
import com.jackappsdev.password_manager.presentation.components.EmptyStateView
import com.jackappsdev.password_manager.presentation.components.LoadingStateView
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.home.components.FilterByCategoryModalSheet
import com.jackappsdev.password_manager.presentation.screens.home.components.PasswordItemsView
import com.jackappsdev.password_manager.presentation.screens.home.components.SortModalSheet
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeEffectHandler
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEffect
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEvent
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeState,
    filterBySheet: SheetState,
    sortBySheet: SheetState,
    lazyColumnState: LazyListState,
    effectFlow: Flow<HomeUiEffect>,
    effectHandler: HomeEffectHandler,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val passwordItems = state.items?.collectAsState()?.value
    val categoryItems = state.categoryItems?.collectAsState()?.value

    if (sortBySheet.isVisible) {
        SortModalSheet(
            sheetState = sortBySheet,
            currentSortBy = state.sortBy,
            onValueChoose = { onEvent(HomeUiEvent.SelectSortBy(it)) }
        )
    }

    if (filterBySheet.isVisible) {
        FilterByCategoryModalSheet(
            sheetState = filterBySheet,
            currentFilterBy = state.filterBy,
            categoryItems = categoryItems ?: listOf(),
            onValueChoose = { onEvent(HomeUiEvent.SelectFilterBy(it)) }
        )
    }

    LaunchedEffect(key1 = Unit) {
        with(effectHandler) {
            effectFlow.collectLatest { effect ->
                when (effect) {
                    is HomeUiEffect.ToggleFilterSheetVisibility -> onToggleFilterSheetVisibility()
                    is HomeUiEffect.ToggleSortSheetVisibility -> onToggleSortSheetVisibility()
                    is HomeUiEffect.ScrollToTop -> onScrollToTop()
                    is HomeUiEffect.LockApplication -> onLockApplication()
                    is HomeUiEffect.NavigateToPasswordDetail -> onNavigateToPasswordDetail(effect.id)
                    is HomeUiEffect.OnSearch -> onSearch()
                    is HomeUiEffect.OnFilterSelected -> onFilterSelected(categoryItems)
                    is HomeUiEffect.OnSortSelected -> onSortSelect(categoryItems)
                    is HomeUiEffect.OnSearchCleared -> onSearchCleared()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onEvent(HomeUiEvent.LockApplication) }) {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = stringResource(R.string.accessibility_lock_application)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_passwords)) },
                actions = {
                    IconButton(onClick = { onEvent(HomeUiEvent.ToggleFilterSheetVisibility) }) {
                        Icon(
                            imageVector = if (state.filterBy == FilterBy.All) {
                                Icons.Outlined.FilterAltOff
                            } else {
                                Icons.Outlined.FilterAlt
                            },
                            contentDescription = stringResource(R.string.accessibility_filter)
                        )
                    }

                    IconButton(onClick = { scope.launch { sortBySheet.show() } }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Sort,
                            contentDescription = stringResource(R.string.accessibility_sort)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onEvent(HomeUiEvent.ScrollToTop)
                    },
                windowInsets = windowInsetsVerticalZero,
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
        val modifier = Modifier.padding(contentPadding)

        when {
            state.isLoading -> {
                LoadingStateView(modifier)
            }

            passwordItems?.isEmpty() == true -> {
                EmptyStateView(modifier = modifier, title = R.string.text_no_passwords)
            }

            else -> {
                PasswordItemsView(
                    modifier = modifier,
                    state = state,
                    lazyColumnState = lazyColumnState,
                    onEvent = onEvent
                )
            }
        }
    }
}
