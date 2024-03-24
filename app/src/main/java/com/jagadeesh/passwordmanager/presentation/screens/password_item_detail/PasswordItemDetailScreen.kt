package com.jagadeesh.passwordmanager.presentation.screens.password_item_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jagadeesh.passwordmanager.core.copyToClipboard
import com.jagadeesh.passwordmanager.core.parseColor
import com.jagadeesh.passwordmanager.presentation.navigation.Routes
import com.jagadeesh.passwordmanager.presentation.theme.pagePadding
import java.text.DateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordItemDetailScreen(
    navController: NavController,
    viewModel: PasswordItemDetailViewModel = hiltViewModel()
) {
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogVisible by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    if (isDeleteDialogVisible) PasswordItemDeleteDialog(
        onConfirm = {
            passwordItem?.let {
                isDeleteDialogVisible = false
                viewModel.deleteItem(it)
                navController.popBackStack()
            }
        },
        onDismiss = { isDeleteDialogVisible = false }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(passwordItem?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            Routes.EditPasswordItem.getPath(
                                passwordItem?.id ?: 0
                            )
                        )
                    }) {
                        Icon(Icons.Filled.Edit, "Edit item")
                    }

                    IconButton(onClick = { isDeleteDialogVisible = true }) {
                        Icon(Icons.Filled.Delete, "Delete item")
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
                value = passwordItem?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.username ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Username") },
                trailingIcon = {
                    IconButton(onClick = { copyToClipboard(context, passwordItem?.username) }) {
                        Icon(Icons.Filled.ContentCopy, "Copy")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.password ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Row {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = "Toggle Password"
                            )
                        }

                        IconButton(onClick = { copyToClipboard(context, passwordItem?.password) }) {
                            Icon(Icons.Filled.ContentCopy, "Copy")
                        }
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
                value = passwordItem?.notes ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                trailingIcon = {
                    IconButton(onClick = { copyToClipboard(context, passwordItem?.notes) }) {
                        Icon(Icons.Filled.ContentCopy, "Copy")
                    }
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {},
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        if (passwordItem?.categoryId == null) {
                            Icon(Icons.Filled.Block, null)
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(parseColor(passwordItem?.categoryColor ?: ""))
                                    .size(24.dp)
                            ) {}
                        }
                    },
                    value = passwordItem?.categoryName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = false, onDismissRequest = {}) {}
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = if (passwordItem?.createdAt != null) {
                    DateFormat.getInstance().format(passwordItem?.createdAt ?: 0)
                } else {
                    ""
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Last Updated At") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
