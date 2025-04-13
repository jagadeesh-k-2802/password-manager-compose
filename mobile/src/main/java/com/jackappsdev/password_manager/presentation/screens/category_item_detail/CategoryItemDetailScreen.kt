package com.jackappsdev.password_manager.presentation.screens.category_item_detail

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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Done
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.constants.colorList
import com.jackappsdev.password_manager.core.parseModifiedTime
import com.jackappsdev.password_manager.presentation.components.CheckmarkCircle
import com.jackappsdev.password_manager.presentation.components.ColoredCircle
import com.jackappsdev.password_manager.presentation.components.UnsavedChangesDialog
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailEffectHandler
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.event.CategoryItemDetailUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItemDetailScreen(
    state: CategoryItemDetailState,
    effectFlow: Flow<CategoryItemDetailUiEffect>,
    effectHandler: CategoryItemDetailEffectHandler,
    onEvent: (CategoryItemDetailUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is CategoryItemDetailUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    if (state.isUnsavedChangesDialogVisible) {
        UnsavedChangesDialog(
            onConfirm = { onEvent(CategoryItemDetailUiEvent.NavigateUp) },
            onDismiss = { onEvent(CategoryItemDetailUiEvent.ToggleUnsavedChangesDialog) }
        )
    }

    if (state.isDeleteDialogVisible) {
        CategoryItemDeleteDialog(
            onConfirm = { onEvent(CategoryItemDetailUiEvent.DeleteCategoryItem) },
            onDismiss = { onEvent(CategoryItemDetailUiEvent.ToggleCategoryItemDeleteDialog) }
        )
    }

    BackHandler(enabled = state.isChanged) {
        onEvent(CategoryItemDetailUiEvent.ToggleUnsavedChangesDialog)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_edit_category)) },
                navigationIcon = {
                    IconButton(onClick = { backDispatcher.onBackPressedDispatcher.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(CategoryItemDetailUiEvent.ToggleCategoryItemDeleteDialog) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.accessibility_delete_item)
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
                value = state.categoryModel?.name ?: EMPTY_STRING,
                onValueChange = { onEvent(CategoryItemDetailUiEvent.OnEnterName(it)) },
                label = { Text(stringResource(R.string.label_name)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.label_category_color), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow {
                items(colorList) { item ->
                    ColoredCircle(
                        modifier = Modifier.padding(end = 12.dp),
                        size = 64.dp,
                        onClick = { onEvent(CategoryItemDetailUiEvent.OnSelectColor(item)) },
                        color = item
                    ) {
                        if (state.categoryModel?.color == item) {
                            CheckmarkCircle()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = if (state.categoryModel?.createdAt != null) {
                    parseModifiedTime(context, state.categoryModel.createdAt)
                } else {
                    EMPTY_STRING
                },
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_last_updated_at)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(CategoryItemDetailUiEvent.UpdateCategoryItem) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Rounded.Done, contentDescription = stringResource(R.string.accessibility_confirm))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_confirm))
            }
        }
    }
}
