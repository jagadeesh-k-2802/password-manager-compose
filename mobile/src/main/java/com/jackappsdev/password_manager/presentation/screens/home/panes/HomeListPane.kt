package com.jackappsdev.password_manager.presentation.screens.home.panes

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.screens.home.HomeScreen
import com.jackappsdev.password_manager.presentation.screens.home.HomeViewModel
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeEffectHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeListPane(
    onNavigateToPasswordItem: (Int) -> Unit,
    onNavigateToAddPassword: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val filterBySheet = rememberModalBottomSheetState()
    val sortBySheet = rememberModalBottomSheetState()
    var showFilterBySheet by remember { mutableStateOf(false) }
    var showSortBySheet by remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val effectHandler = remember {
        HomeEffectHandler(
            context = context,
            scope = scope,
            filterBySheet = filterBySheet,
            sortBySheet = sortBySheet,
            lazyColumnState = lazyColumnState,
            keyboardController = keyboardController,
            focusManager = focusManager,
            onShowFilterBySheet = { showFilterBySheet = it },
            onShowSortBySheet = { showSortBySheet = it },
            navigateToPasswordItem = onNavigateToPasswordItem,
            navigateToAddPassword = onNavigateToAddPassword
        )
    }

    HomeScreen(
        state = viewModel.state,
        filterBySheet = filterBySheet,
        sortBySheet = sortBySheet,
        showFilterBySheet = showFilterBySheet,
        showSortBySheet = showSortBySheet,
        onDismissFilterSheet = { showFilterBySheet = false },
        onDismissSortSheet = { showSortBySheet = false },
        lazyColumnState = lazyColumnState,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
