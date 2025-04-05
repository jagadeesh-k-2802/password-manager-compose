package com.jackappsdev.password_manager.presentation.screens.manage_categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.components.EmptyStateView
import com.jackappsdev.password_manager.presentation.components.LoadingStateView
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.manage_categories.components.CategoryItemsView
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesEffectHandler
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesUiEffect
import com.jackappsdev.password_manager.presentation.screens.manage_categories.event.ManageCategoriesUiEvent
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoriesScreen(
    navController: NavController,
    state: ManageCategoriesState,
    effectFlow: Flow<ManageCategoriesUiEffect>,
    effectHandler: ManageCategoriesEffectHandler,
    onEvent: (ManageCategoriesUiEvent) -> Unit
) {
    val categoryItems = state.items?.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when(effect) {
                    ManageCategoriesUiEffect.ScrollToTop -> onScrollToTop(lazyColumnState)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_categories)) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onEvent(ManageCategoriesUiEvent.ScrollToTop)
                },
                windowInsets = windowInsetsVerticalZero
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.AddCategoryItem) }) {
                Icon(Icons.Rounded.Add, stringResource(R.string.accessibility_add_category))
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

            categoryItems?.value?.isEmpty() == true -> {
                EmptyStateView(modifier = modifier, title = R.string.text_no_categories_available)
            }

            else -> {
                CategoryItemsView(modifier, lazyColumnState, navController, categoryItems)
            }
        }
    }
}
