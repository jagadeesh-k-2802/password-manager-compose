package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.components.ItemView
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailEffectHandler
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PasswordItemDetailScreen(
    state: PasswordItemDetailState,
    effectFlow: Flow<PasswordItemDetailUiEffect>,
    effectHandler: PasswordItemDetailEffectHandler,
    onEvent: (PasswordItemDetailUiEvent) -> Unit,
) {
    val passwordItem = state.passwordItem?.collectAsState()?.value

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is PasswordItemDetailUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Icon,
            last = ScalingLazyColumnDefaults.ItemType.Card
        )
    )

    LaunchedEffect(state.passwordItem) {
        if (state.passwordItem?.value == null && state.isValueAlreadySetOnce) {
            onEvent(PasswordItemDetailUiEvent.NavigateUp)
        } else {
            onEvent(PasswordItemDetailUiEvent.ToggleAlreadySetOnce)
        }
    }

    ScreenScaffold(scrollState = columnState) {
        ItemView(passwordItem, columnState)
    }
}
