package com.jackappsdev.password_manager.presentation.screens.edit_password_item

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Done
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.navigation.navigate
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordItemScreen(
    navController: NavController,
    viewModel: EditPasswordItemViewModel = hiltViewModel()
) {
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)
    val categoryItems by viewModel.categoryItems.collectAsState(initial = listOf())
    var name by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.name ?: "") }
    var username by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.username ?: "") }
    var password by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.password ?: "") }
    var notes by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.notes ?: "") }
    var isChanged by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    var category by remember(passwordItem) {
        mutableStateOf(
            CategoryModel(
                id = passwordItem?.categoryId,
                name = passwordItem?.categoryName ?: "",
                color = passwordItem?.categoryColor ?: "",
            )
        )
    }

    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isUnsavedChangesDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isCategoryDropdownVisible by rememberSaveable { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val dispatcher = backDispatcher.onBackPressedDispatcher

    val backCallback = remember(name, username, password, notes, category, passwordItem) {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isChanged) {
                    isUnsavedChangesDialogVisible = true
                } else {
                    navController.popBackStack()
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner, backDispatcher) {
        dispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose { backCallback.remove() }
    }

    if (isUnsavedChangesDialogVisible) UnsavedChangesDialog(
        onConfirm = { navController.popBackStack() },
        onDismiss = { isUnsavedChangesDialogVisible = false }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Item") },
                navigationIcon = {
                    IconButton(onClick = { backCallback.handleOnBackPressed() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Go back")
                    }
                }
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
                    name = value
                    isChanged = true
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { value ->
                    username = value
                    isChanged = true
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { value ->
                    password = value
                    isChanged = true
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) {
                                Icons.Outlined.VisibilityOff
                            } else {
                                Icons.Outlined.Visibility
                            },
                            contentDescription = "Toggle Password"
                        )
                    }
                },
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { value ->
                    notes = value
                    isChanged = true
                },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = isCategoryDropdownVisible,
                onExpandedChange = { value -> isCategoryDropdownVisible = value },
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
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownVisible)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = isCategoryDropdownVisible,
                    onDismissRequest = { isCategoryDropdownVisible = false },
                ) {
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Outlined.Block, "No category") },
                        text = { Text(text = "No Category") },
                        onClick = {
                            category = CategoryModel(name = "No Category", color = "")
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
                        leadingIcon = { Icon(Icons.Outlined.Add, "Add category") },
                        text = { Text(text = "Create New Category") },
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
                    viewModel.onEditComplete(
                        name,
                        username,
                        password,
                        notes,
                        category.id,
                        passwordItem
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Done, "Confirm Edit")
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Confirm")
            }
        }
    }
}
