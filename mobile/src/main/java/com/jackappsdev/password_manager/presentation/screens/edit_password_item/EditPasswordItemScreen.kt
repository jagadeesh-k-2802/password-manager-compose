package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemDto
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.composables.UnsavedChangesDialog
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.add_category_item.CREATED_CATEGORY
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowinsetsVerticalZero
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordItemScreen(
    navController: NavController,
    viewModel: EditPasswordItemViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)
    val categoryItems by viewModel.categoryItems.collectAsState(initial = listOf())
    val error by viewModel.errorChannel.receiveAsFlow().collectAsState(initial = null)
    var isChanged by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isUnsavedChangesDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isAlreadyAutoFocused by rememberSaveable { mutableStateOf(false) }
    var isCategoryDropdownVisible by rememberSaveable { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val dispatcher = backDispatcher.onBackPressedDispatcher
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val savedStateHandle = navController.currentBackStackEntryAsState().value?.savedStateHandle

    var username by remember(passwordItem) {
        mutableStateOf(
            TextFieldValue(
                passwordItem?.username ?: "", TextRange(passwordItem?.username?.length ?: 0)
            )
        )
    }

    var name by remember(passwordItem) {
        mutableStateOf(
            TextFieldValue(
                passwordItem?.name ?: "", TextRange(passwordItem?.name?.length ?: 0)
            )
        )
    }

    var password by remember(passwordItem) {
        mutableStateOf(
            TextFieldValue(
                passwordItem?.password ?: "", TextRange(passwordItem?.password?.length ?: 0)
            )
        )
    }

    var website by remember(passwordItem) {
        mutableStateOf(
            TextFieldValue(
                passwordItem?.website ?: "", TextRange(passwordItem?.website?.length ?: 0)
            )
        )
    }

    var notes by remember(passwordItem) {
        mutableStateOf(
            TextFieldValue(passwordItem?.notes ?: "", TextRange(passwordItem?.notes?.length ?: 0))
        )
    }

    var category by remember(passwordItem, savedStateHandle) {
        mutableStateOf(
            if (savedStateHandle?.contains(CREATED_CATEGORY) == true) Json.decodeFromString(
                savedStateHandle[CREATED_CATEGORY] ?: ""
            )
            else
                CategoryModel(
                    id = passwordItem?.categoryId,
                    name = passwordItem?.categoryName ?: "",
                    color = passwordItem?.categoryColor ?: "",
                )
        )
    }

    val backCallback = remember(name, username, password, notes, category, passwordItem) {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isChanged) {
                    isUnsavedChangesDialogVisible = true
                } else {
                    navController.navigateUp()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!isAlreadyAutoFocused) {
            focusRequester.requestFocus()
            isAlreadyAutoFocused = true
        }
    }

    DisposableEffect(lifecycleOwner, backDispatcher) {
        dispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose { backCallback.remove() }
    }

    if (isUnsavedChangesDialogVisible) UnsavedChangesDialog(
        onConfirm = { navController.navigateUp() },
        onDismiss = { isUnsavedChangesDialogVisible = false }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_edit_item)) },
                navigationIcon = {
                    IconButton(onClick = { backCallback.handleOnBackPressed() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                windowInsets = windowinsetsVerticalZero
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
                value = name,
                onValueChange = { value ->
                    if (name.text != value.text) isChanged = true
                    name = value
                },
                isError = error is EditPasswordItemError.NameError,
                supportingText = {
                    error?.let {
                        if (it is EditPasswordItemError.NameError) Text(stringResource(it.error))
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { value -> username = value },
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
                value = password,
                onValueChange = { value ->
                    if (password.text != value.text) isChanged = true
                    password = value
                },
                label = { Text(stringResource(R.string.label_password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Row {
                        IconButton(onClick = {
                            password = TextFieldValue(
                                text = generateRandomPassword(GeneratePasswordConfig(length = 12)),
                                selection = TextRange(index = 12)
                            )
                        }) {
                            Icon(
                                Icons.Outlined.Refresh,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }

                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = website,
                onValueChange = { value -> website = value },
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
                value = notes,
                onValueChange = { value ->
                    if (notes.text != value.text) isChanged = true
                    notes = value
                },
                label = { Text(stringResource(R.string.label_notes)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Default
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = isCategoryDropdownVisible,
                onExpandedChange = { value -> isCategoryDropdownVisible = value }
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        if (category.id == null) {
                            Icon(Icons.Outlined.Block, null)
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(parseColor(category.color))
                                    .size(24.dp)
                            ) {}
                        }
                    },
                    value = category.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_category)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownVisible)
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = isCategoryDropdownVisible,
                    onDismissRequest = { isCategoryDropdownVisible = false },
                    modifier = Modifier.requiredSizeIn(maxHeight = 150.dp)
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Block,
                                stringResource(R.string.text_no_category)
                            )
                        },
                        text = { Text(text = stringResource(R.string.text_no_category)) },
                        onClick = {
                            category = CategoryModel(
                                name = getString(context, R.string.text_no_category),
                                color = ""
                            )
                            isChanged = true
                            isCategoryDropdownVisible = false
                        }
                    )

                    categoryItems.forEach { item ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(parseColor(item.color))
                                        .size(24.dp)
                                ) {}
                            },
                            text = { Text(text = item.name) },
                            onClick = {
                                category = item
                                isChanged = true
                                isCategoryDropdownVisible = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Add,
                                stringResource(R.string.accessibility_add_category)
                            )
                        },
                        text = { Text(text = stringResource(R.string.label_create_new_category)) },
                        onClick = {
                            navController.navigate(Routes.AddCategoryItem)
                            isCategoryDropdownVisible = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    keyboardController?.hide()

                    viewModel.onEditComplete(
                        name.text,
                        username.text,
                        password.text,
                        website.text,
                        notes.text,
                        category.id,
                        passwordItem
                    ) { newPasswordItemModel ->
                        if (passwordItem?.isAddedToWatch == false) {
                            navController.navigateUp()
                            return@onEditComplete
                        }

                        val dataClient = Wearable.getDataClient(context)

                        val putDataRequest = PutDataMapRequest.create(UPSERT_PASSWORD).run {
                            dataMap.putString(
                                KEY_PASSWORD,
                                Json.encodeToString(newPasswordItemModel.toPasswordItemDto())
                            )
                            setUrgent()
                            asPutDataRequest()
                        }

                        dataClient.putDataItem(putDataRequest).addOnCompleteListener {
                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_confirm))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_confirm))
            }
        }
    }
}
