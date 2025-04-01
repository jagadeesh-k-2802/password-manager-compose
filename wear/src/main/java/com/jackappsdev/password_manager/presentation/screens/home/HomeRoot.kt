package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeRoot(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()

    HomeScreen(
        navController = navController,
        state = viewModel.state
    )
}
