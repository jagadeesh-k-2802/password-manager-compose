package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemEffectHandler

@Composable
fun EditPasswordItemRoot(
    navController: NavController
) {
    val viewModel: EditPasswordItemViewModel = hiltViewModel()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val effectHandler = remember {
        EditPasswordItemEffectHandler(
            context = context,
            navController = navController,
            keyboardController = keyboardController
        )
    }

    EditPasswordItemScreen(
        navController = navController,
        state = viewModel.state,
        categoryItems = viewModel.categoryItems.collectAsState(listOf()),
        errorFlow = viewModel.errorFlow,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
