package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.core.copyToClipboard
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.theme.disabledButEnabledOutlinedTextFieldColors
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val scrollState = rememberScrollState()

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
                title = {
                    Text(
                        passwordItem?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Go back")
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
                        Icon(Icons.Outlined.Edit, "Edit item")
                    }

                    IconButton(onClick = { isDeleteDialogVisible = true }) {
                        Icon(Icons.Outlined.Delete, "Delete item")
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
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = passwordItem?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Name") },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.username ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Username") },
                trailingIcon = {
                    IconButton(onClick = { copyToClipboard(context, passwordItem?.username) }) {
                        Icon(Icons.Outlined.ContentCopy, "Copy")
                    }
                },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.password ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                trailingIcon = {
                    Row {
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

                        IconButton(onClick = { copyToClipboard(context, passwordItem?.password) }) {
                            Icon(Icons.Outlined.ContentCopy, "Copy")
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
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                trailingIcon = {
                    IconButton(onClick = { copyToClipboard(context, passwordItem?.notes) }) {
                        Icon(Icons.Outlined.ContentCopy, "Copy")
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
                            Icon(Icons.Outlined.Block, null)
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
                    enabled = false,
                    colors = disabledButEnabledOutlinedTextFieldColors(),
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
                    val is24Hours = DateFormat.is24HourFormat(context)
                    val hours = if (is24Hours) "HH" else "hh"
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy $hours:mm", Locale.ENGLISH)
                    dateFormat.format(Date(passwordItem?.createdAt!!))
                } else {
                    ""
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Last Updated At") },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
