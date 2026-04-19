package com.jackappsdev.password_manager.presentation.screens.home.panes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailScreen
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailViewModel
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailEffectHandler

@Composable
internal fun DetailPanePasswordDetail(
    id: Int,
    showBackButton: Boolean,
    onEditRequested: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val key = Routes.PasswordItemDetail(id)
    val viewModel = hiltViewModel<PasswordItemDetailViewModel, PasswordItemDetailViewModel.Factory>(
        key = "detail_$id",
        creationCallback = { factory -> factory.create(key) }
    )
    val context = LocalContext.current

    val effectHandler = remember(id, onEditRequested, onNavigateUp) {
        PasswordItemDetailEffectHandler(
            context = context,
            onEvent = viewModel::onEvent,
            navigateToEditPassword = { onEditRequested() },
            navigateUp = onNavigateUp
        )
    }

    PasswordItemDetailScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent,
        showBackButton = showBackButton
    )
}
