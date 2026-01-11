package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemEffectHandler

@Composable
fun EditPasswordItemRoot(navigator: Navigator, key: Routes.EditPasswordItem) {
    val viewModel = hiltViewModel<EditPasswordItemViewModel, EditPasswordItemViewModel.Factory>(
        creationCallback = {
                factory -> factory.create(key)
        }
    )
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val effectHandler = remember {
        EditPasswordItemEffectHandler(
            context = context,
            navigator = navigator,
            keyboardController = keyboardController
        )
    }

    EditPasswordItemScreen(
        state = viewModel.state,
        errorFlow = viewModel.errorFlow,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
