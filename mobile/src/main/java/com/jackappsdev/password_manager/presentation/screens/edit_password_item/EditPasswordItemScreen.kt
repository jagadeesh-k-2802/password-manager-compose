package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.components.UnsavedChangesDialog
import com.jackappsdev.password_manager.presentation.screens.add_category_item.constants.CREATED_CATEGORY
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.components.CategoryDropDown
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemEffectHandler
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import com.jackappsdev.password_manager.shared.constants.ZERO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordItemScreen(
    savedStateHandle: SavedStateHandle?,
    state: EditPasswordItemState,
    errorFlow: Flow<EditPasswordItemError>,
    effectFlow: Flow<EditPasswordItemUiEffect>,
    effectHandler: EditPasswordItemEffectHandler,
    onEvent: (EditPasswordItemUiEvent) -> Unit
) {
    val categoryItems = state.categoryItems?.collectAsState(initial = listOf())?.value
    val error by errorFlow.collectAsState(initial = null)
    val scrollState = rememberScrollState()
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = state.passwordItem) {
        if (!state.isAlreadyAutoFocused) {
            focusRequester.requestFocus()
            onEvent(EditPasswordItemUiEvent.ToggleAlreadyAutoFocused)
        }

        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when (effect) {
                    is EditPasswordItemUiEffect.EditComplete -> onEditComplete(state.passwordItem)
                    is EditPasswordItemUiEffect.NavigateToAddCategory -> onNavigateToAddCategory()
                    is EditPasswordItemUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    LaunchedEffect(key1 = savedStateHandle) {
        if (savedStateHandle?.contains(CREATED_CATEGORY) == true) {
            val json = savedStateHandle[CREATED_CATEGORY] ?: EMPTY_STRING
            val model = Json.decodeFromString<CategoryModel>(json)
            onEvent(EditPasswordItemUiEvent.SelectCategory(model))
        }
    }

    if (state.isUnsavedChangesDialogVisible) {
        UnsavedChangesDialog(
            onConfirm = { onEvent(EditPasswordItemUiEvent.NavigateUp) },
            onDismiss = { onEvent(EditPasswordItemUiEvent.ToggleUnsavedChangesDialogVisibility) }
        )
    }

    BackHandler(enabled = state.isChanged) {
        onEvent(EditPasswordItemUiEvent.ToggleUnsavedChangesDialogVisibility)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_edit_item)) },
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
                .imePadding()
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = remember(state.passwordItem?.name) {
                    TextFieldValue(
                        text = state.passwordItem?.name ?: EMPTY_STRING,
                        selection = TextRange(state.passwordItem?.name?.length ?: ZERO)
                    )
                },
                onValueChange = { onEvent(EditPasswordItemUiEvent.EnterName(it.text)) },
                isError = error is EditPasswordItemError.NameError,
                supportingText = {
                    error?.let {
                        if (it is EditPasswordItemError.NameError) Text(stringResource(it.error))
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Badge, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            OutlinedTextField(
                value = remember(state.passwordItem?.username) {
                    TextFieldValue(
                        text = state.passwordItem?.username ?: EMPTY_STRING,
                        selection = TextRange(state.passwordItem?.username?.length ?: ZERO)
                    )
                },
                onValueChange = { onEvent(EditPasswordItemUiEvent.EnterUsername(it.text)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_username)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = remember(state.passwordItem?.password) {
                    TextFieldValue(
                        text = state.passwordItem?.password ?: EMPTY_STRING,
                        selection = TextRange(state.passwordItem?.password?.length ?: ZERO)
                    )
                },
                onValueChange = { onEvent(EditPasswordItemUiEvent.EnterPassword(it.text)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Key, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (state.showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    Row {
                        IconButton(onClick = { onEvent(EditPasswordItemUiEvent.GenerateRandomPassword) }) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }

                        IconButton(onClick = { onEvent(EditPasswordItemUiEvent.ToggleShowPassword) }) {
                            Icon(
                                if (state.showPassword) {
                                    Icons.Outlined.VisibilityOff
                                } else {
                                    Icons.Outlined.Visibility
                                },
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = remember(state.passwordItem?.website) {
                    TextFieldValue(
                        text = state.passwordItem?.website ?: EMPTY_STRING,
                        selection = TextRange(state.passwordItem?.website?.length ?: ZERO)
                    )
                },
                onValueChange = { onEvent(EditPasswordItemUiEvent.EnterWebsite(it.text)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Link, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_website)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = remember(state.passwordItem?.notes) {
                    TextFieldValue(
                        text = state.passwordItem?.notes ?: EMPTY_STRING,
                        selection = TextRange(state.passwordItem?.notes?.length ?: ZERO)
                    )
                },
                onValueChange = { onEvent(EditPasswordItemUiEvent.EnterNotes(it.text)) },
                leadingIcon = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_notes)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Default
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryDropDown(
                state = state,
                categoryItems = categoryItems,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onEvent(EditPasswordItemUiEvent.EditPassword) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Outlined.Done, contentDescription = stringResource(R.string.accessibility_confirm))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_confirm))
            }
        }
    }
}
