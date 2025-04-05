package com.jackappsdev.password_manager.presentation.screens.add_password_item

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.components.UnsavedChangesDialog
import com.jackappsdev.password_manager.presentation.screens.add_category_item.constants.CREATED_CATEGORY
import com.jackappsdev.password_manager.presentation.screens.add_password_item.components.CategoryDropDown
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemEffectHandler
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEffect
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEvent
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordItemScreen(
    navController: NavController,
    state: AddPasswordItemState,
    categoryItems: State<List<CategoryModel>>,
    errorFlow: Flow<AddPasswordItemError>,
    effectFlow: Flow<AddPasswordItemUiEffect>,
    effectHandler: AddPasswordItemEffectHandler,
    onEvent: (AddPasswordItemUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val dispatcher = backDispatcher.onBackPressedDispatcher
    val savedStateHandle = navController.currentBackStackEntryAsState().value?.savedStateHandle
    val error by errorFlow.collectAsState(initial = null)

    val backCallback = remember(state.hasUserEnteredDetails) {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (state.hasUserEnteredDetails) {
                    onEvent(AddPasswordItemUiEvent.ToggleIsUnsavedDialogVisibility)
                } else {
                    navController.navigateUp()
                }
            }
        }
    }

    LaunchedEffect(key1 = savedStateHandle) {
        if (savedStateHandle?.contains(CREATED_CATEGORY) == true) {
            val json = savedStateHandle[CREATED_CATEGORY] ?: ""
            val model = Json.decodeFromString<CategoryModel>(json)
            onEvent(AddPasswordItemUiEvent.OnSelectCategory(model))
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!state.isAlreadyAutoFocused) {
            focusRequester.requestFocus()
            onEvent(AddPasswordItemUiEvent.SetIsAlreadyAutoFocus)
        }

        effectFlow.collectLatest { effect ->
            with(effectHandler) {
                when(effect) {
                    AddPasswordItemUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner, backDispatcher) {
        dispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose { backCallback.remove() }
    }

    if (state.isUnsavedChangesDialogVisible) {
        UnsavedChangesDialog(
            onConfirm = { navController.navigateUp() },
            onDismiss = { onEvent(AddPasswordItemUiEvent.ToggleIsUnsavedDialogVisibility) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_add_new_password)) },
                navigationIcon = {
                    IconButton(onClick = { backCallback.handleOnBackPressed() }) {
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
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(AddPasswordItemUiEvent.OnEnterName(it)) },
                isError = error is AddPasswordItemError.NameError,
                supportingText = {
                    error?.let {
                        if (it is AddPasswordItemError.NameError) Text(stringResource(it.error))
                    }
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
                value = state.username,
                onValueChange = { onEvent(AddPasswordItemUiEvent.OnEnterUsername(it)) },
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(AddPasswordItemUiEvent.OnEnterPassword(it)) },
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
                        IconButton(onClick = { onEvent(AddPasswordItemUiEvent.OnGenerateRandomPassword) }) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }

                        IconButton(onClick = { onEvent(AddPasswordItemUiEvent.ToggleShowPassword) }) {
                            Icon(
                                imageVector = if (state.showPassword) {
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.website,
                onValueChange = { onEvent(AddPasswordItemUiEvent.OnEnterWebsite(it)) },
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.notes,
                onValueChange = { onEvent(AddPasswordItemUiEvent.OnEnterNotes(it)) },
                label = { Text(stringResource(R.string.label_notes)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Default
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            CategoryDropDown(
                navController = navController,
                state = state,
                categoryItems = categoryItems,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onEvent(AddPasswordItemUiEvent.AddPasswordItem) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_create))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_create))
            }
        }
    }
}
