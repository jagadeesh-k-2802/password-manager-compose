package com.jackappsdev.password_manager.presentation.screens.add_password_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemEffectHandler

@Composable
fun AddPasswordItemRoot(navController: NavController) {
    val viewModel: AddPasswordItemViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val savedStateHandle = navController.currentBackStackEntryAsState().value?.savedStateHandle

    val effectHandler = remember {
        AddPasswordItemEffectHandler(
            navController = navController,
            keyboardController = keyboardController
        )
    }

    AddPasswordItemScreen(
        savedStateHandle = savedStateHandle,
        state = viewModel.state,
        categoryItems = viewModel.categoryItems.collectAsState(listOf()),
        errorFlow = viewModel.errorFlow,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
