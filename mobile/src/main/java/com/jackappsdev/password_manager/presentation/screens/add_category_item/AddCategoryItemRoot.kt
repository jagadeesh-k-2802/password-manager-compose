package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.LocalResultEventBus
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemEffectHandler

@Composable
fun AddCategoryItemRoot(navigator: Navigator) {
    val viewModel: AddCategoryItemViewModel = hiltViewModel()
    val resultEventBus = LocalResultEventBus.current

    val effectHandler = remember {
        AddCategoryItemEffectHandler(
            navigator = navigator,
            resultEventBus = resultEventBus
        )
    }

    AddCategoryItemScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        errorFlow = viewModel.errorFlow,
        onEvent = viewModel::onEvent
    )
}
