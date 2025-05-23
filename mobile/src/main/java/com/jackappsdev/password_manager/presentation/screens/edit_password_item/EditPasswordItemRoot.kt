package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemEffectHandler

@Composable
fun EditPasswordItemRoot(
    navController: NavController
) {
    val viewModel: EditPasswordItemViewModel = hiltViewModel()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val savedStateHandle = navController.currentBackStackEntryAsState().value?.savedStateHandle

    val effectHandler = remember {
        EditPasswordItemEffectHandler(
            context = context,
            navController = navController,
            keyboardController = keyboardController
        )
    }

    EditPasswordItemScreen(
        savedStateHandle = savedStateHandle,
        state = viewModel.state,
        errorFlow = viewModel.errorFlow,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
