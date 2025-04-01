package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun PasswordItemDetailRoot(navController: NavController) {
    val viewModel: PasswordItemDetailViewModel = hiltViewModel()

    PasswordItemDetailScreen(
        navController = navController,
        passwordItem = viewModel.passwordItem.collectAsState(null),
    )
}
