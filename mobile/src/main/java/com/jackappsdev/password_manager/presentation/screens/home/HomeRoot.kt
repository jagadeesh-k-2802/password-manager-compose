package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeEffectHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoot(navController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val filterBySheet = rememberModalBottomSheetState()
    val sortBySheet = rememberModalBottomSheetState()
    val lazyColumnState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val effectHandler = remember {
        HomeEffectHandler(
            context = context,
            navController = navController,
            scope = scope,
            filterBySheet = filterBySheet,
            sortBySheet = sortBySheet,
            lazyColumnState = lazyColumnState,
            keyboardController = keyboardController,
            focusManager = focusManager
        )
    }

    HomeScreen(
        state = viewModel.state,
        filterBySheet = filterBySheet,
        sortBySheet = sortBySheet,
        lazyColumnState = lazyColumnState,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
