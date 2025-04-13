package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.constants.colorList
import com.jackappsdev.password_manager.presentation.components.CheckmarkCircle
import com.jackappsdev.password_manager.presentation.components.ColoredCircle
import com.jackappsdev.password_manager.presentation.components.UnsavedChangesDialog
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemEffectHandler
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryItemScreen(
    navController: NavController,
    state: AddCategoryItemState,
    errorFlow: Flow<AddCategoryItemError>,
    effectFlow: Flow<AddCategoryItemUiEffect>,
    effectHandler: AddCategoryItemEffectHandler,
    onEvent: (AddCategoryItemUiEvent) -> Unit
) {
    val error by errorFlow.collectAsState(initial = null)
    val scrollState = rememberScrollState()
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()

        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is AddCategoryItemUiEffect.NavigateUp -> onNavigateUp(effect.model)
                }
            }
        }
    }

    if (state.isUnsavedChangesDialogVisible) {
        UnsavedChangesDialog(
            onConfirm = { navController.navigateUp() },
            onDismiss = { onEvent(AddCategoryItemUiEvent.ToggleUnsavedChangesDialogVisibility) }
        )
    }

    BackHandler(enabled = state.name.isNotEmpty()) {
        onEvent(AddCategoryItemUiEvent.ToggleUnsavedChangesDialogVisibility)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_add_new_category)) },
                navigationIcon = {
                    IconButton(onClick = { backDispatcher.onBackPressedDispatcher.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                windowInsets = windowInsetsVerticalZero
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = state.name,
                isError = error is AddCategoryItemError.NameError,
                supportingText = {
                    error?.let {
                        if (it is AddCategoryItemError.NameError) Text(stringResource(it.error))
                    }
                },
                onValueChange = { onEvent(AddCategoryItemUiEvent.OnEnterName(it)) },
                label = { Text(stringResource(R.string.label_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.label_category_color), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow {
                items(colorList) { item ->
                    ColoredCircle(
                        modifier = Modifier.padding(end = 12.dp),
                        size = 64.dp,
                        onClick = { onEvent(AddCategoryItemUiEvent.OnSelectColor(item)) },
                        color = item,
                    ) {
                        if (state.color == item) {
                            CheckmarkCircle()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(AddCategoryItemUiEvent.AddCategoryItem) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_create))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_create))
            }
        }
    }
}
