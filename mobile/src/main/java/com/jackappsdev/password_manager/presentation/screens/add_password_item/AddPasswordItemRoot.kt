package com.jackappsdev.password_manager.presentation.screens.add_password_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemEffectHandler

@Composable
fun AddPasswordItemRoot(navigator: Navigator) {
    val viewModel: AddPasswordItemViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current

    val effectHandler = remember {
        AddPasswordItemEffectHandler(
            navigator = navigator,
            keyboardController = keyboardController
        )
    }

    AddPasswordItemScreen(
        state = viewModel.state,
        errorFlow = viewModel.errorFlow,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
    )
}
