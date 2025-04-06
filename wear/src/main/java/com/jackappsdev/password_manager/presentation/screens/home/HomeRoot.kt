package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeEffectHandler

@Composable
fun HomeRoot(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()

    val effectHandler = remember {
        HomeEffectHandler(
            navController = navController
        )
    }

    HomeScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
