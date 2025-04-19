package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemEffectHandler

@Composable
fun AddCategoryItemRoot(navController: NavController) {
    val viewModel: AddCategoryItemViewModel = hiltViewModel()

    val effectHandler = remember {
        AddCategoryItemEffectHandler(
            navController = navController
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
