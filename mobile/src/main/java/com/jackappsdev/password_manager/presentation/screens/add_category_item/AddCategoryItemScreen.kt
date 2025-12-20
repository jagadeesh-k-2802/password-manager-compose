package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Category
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
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.constants.colorList
import com.jackappsdev.password_manager.presentation.components.CheckmarkCircle
import com.jackappsdev.password_manager.presentation.components.ColoredCircle
import com.jackappsdev.password_manager.presentation.components.ConfirmationDialog
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemEffectHandler
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.add_category_item.event.AddCategoryItemUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryItemScreen(
    state: AddCategoryItemState,
    effectFlow: Flow<AddCategoryItemUiEffect>,
    effectHandler: AddCategoryItemEffectHandler,
    errorFlow: Flow<AddCategoryItemError>,
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
        ConfirmationDialog(
            title = R.string.dialog_title_unsaved_changes,
            description = R.string.dialog_text_unsaved,
            onConfirm = { onEvent(AddCategoryItemUiEvent.NavigateUp) },
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
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
                .imePadding()
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
                onValueChange = { onEvent(AddCategoryItemUiEvent.EnterName(it)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Category, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            Text(text = stringResource(R.string.label_category_color), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow {
                items(colorList) { item ->
                    ColoredCircle(
                        modifier = Modifier.padding(end = 12.dp),
                        size = 48.dp,
                        onClick = { onEvent(AddCategoryItemUiEvent.SelectColor(item)) },
                        color = item,
                    ) {
                        if (state.color == item) {
                            CheckmarkCircle()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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
