package com.jackappsdev.password_manager.presentation.screens.home.panes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemScreen
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemViewModel
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemEffectHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailPaneEditPassword(
    id: Int,
    editVersion: Int,
    navigator: Navigator,
    onEditComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val key = Routes.EditPasswordItem(id)
    val viewModel = hiltViewModel<EditPasswordItemViewModel, EditPasswordItemViewModel.Factory>(
        key = "edit_${id}_$editVersion",
        creationCallback = { factory -> factory.create(key) }
    )
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val effectHandler = remember(id, onEditComplete, onCancel) {
        EditPasswordItemEffectHandler(
            context = context,
            keyboardController = keyboardController,
            navigateToAddCategory = { navigator.navigate(Routes.AddCategoryItem) },
            navigateUp = onEditComplete
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
