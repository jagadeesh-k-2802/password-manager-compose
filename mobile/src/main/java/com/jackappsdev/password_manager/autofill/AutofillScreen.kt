package com.jackappsdev.password_manager.autofill

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.autofill.components.AutofillSelectionList
import com.jackappsdev.password_manager.presentation.components.EmptyStateView
import com.jackappsdev.password_manager.presentation.components.LoadingStateView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutofillScreen(
    state: AutofillState,
    effectFlow: Flow<AutofillUiEffect>,
    effectHandler: AutofillEffectHandler,
    onEvent: (AutofillUiEvent) -> Unit
) {
    val passwordItems = state.items?.collectAsState()?.value

    LaunchedEffect(Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is AutofillUiEffect.ItemSelected -> onItemSelected(effect.item)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_autofill_select_password)) }
            )
        }
    ) { contentPadding ->
        val modifier = Modifier.padding(contentPadding).fillMaxSize()

        when {
            state.isLoading -> {
                LoadingStateView(modifier)
            }

            passwordItems.isNullOrEmpty() -> {
                EmptyStateView(
                    modifier = modifier,
                    title = R.string.text_no_passwords
                )
            }

            else -> {
                AutofillSelectionList(
                    modifier = modifier,
                    state = state,
                    onEvent = onEvent
                )
            }
        }
    }
}
