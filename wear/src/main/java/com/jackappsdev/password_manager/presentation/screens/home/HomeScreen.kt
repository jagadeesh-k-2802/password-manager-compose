package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.components.EmptyStateView
import com.jackappsdev.password_manager.presentation.components.LoadingStateView
import com.jackappsdev.password_manager.presentation.screens.home.components.PasswordItemsView
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeEffectHandler
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEffect
import com.jackappsdev.password_manager.presentation.screens.home.event.HomeUiEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun HomeScreen(
    state: HomeState,
    effectFlow: Flow<HomeUiEffect>,
    effectHandler: HomeEffectHandler,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val passwordItems = state.items?.collectAsState()?.value

    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Icon,
            last = ScalingLazyColumnDefaults.ItemType.Chip
        )
    )

    LaunchedEffect(key1 = Unit) {
        effectFlow.collect { effect ->
            with(effectHandler) {
                when (effect) {
                    is HomeUiEffect.NavigateToPasswordDetail -> onNavigateToPasswordDetail(effect.id)
                }
            }
        }
    }

    ScreenScaffold(scrollState = columnState) {
        when {
            state.isLoading -> LoadingStateView()
            passwordItems?.isEmpty() == true -> EmptyStateView(text = stringResource(R.string.text_no_passwords))
            else -> PasswordItemsView(columnState, passwordItems, onEvent)
        }
    }
}
