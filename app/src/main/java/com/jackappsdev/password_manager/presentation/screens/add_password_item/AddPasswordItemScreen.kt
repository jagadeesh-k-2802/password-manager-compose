package com.jackappsdev.password_manager.presentation.screens.add_password_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordItemScreen(
    navController: NavController,
    viewModel: AddPasswordItemViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isCategoryDropdownVisible by rememberSaveable { mutableStateOf(false) }
    var category by remember { mutableStateOf(CategoryModel(name = "No Category", color = "")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val categoryItems by viewModel.categoryItems.collectAsState(initial = listOf())
    val error by viewModel.errorChannel.receiveAsFlow().collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add New Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { value -> name = value },
                isError = error is AddPasswordItemError.NameError,
                supportingText = {
                    error?.let {
                        if (it is AddPasswordItemError.NameError) Text(text = it.error)
                    }
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { value -> username = value },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { value -> password = value },
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
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { value -> notes = value },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = isCategoryDropdownVisible,
                onExpandedChange = { value -> isCategoryDropdownVisible = value },
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        if (category.name == "No Category") {
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()

                    viewModel.addPasswordItem(name, username, password, notes, category) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Done, "Create")
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Create")
            }
        }
    }
}
