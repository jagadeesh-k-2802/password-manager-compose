package com.jagadeesh.passwordmanager.presentation.screens.edit_item

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jagadeesh.passwordmanager.presentation.theme.pagePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    navController: NavController,
    viewModel: EditItemViewModel = hiltViewModel()
) {
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)
    var name by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.name ?: "") }
    var username by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.username ?: "") }
    var password by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.password ?: "") }
    var notes by rememberSaveable(passwordItem) { mutableStateOf(passwordItem?.notes ?: "") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isUnsavedChangesDialogVisible by rememberSaveable { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val dispatcher = backDispatcher.onBackPressedDispatcher

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("HELLO WORLD")
                if (viewModel.hasChanges(name, username, password, notes, passwordItem)) {
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
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
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { value -> username = value },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { value -> password = value },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
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
                onValueChange = { value -> notes = value },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.onEditComplete(name, username, password, notes, passwordItem) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Done, "Confirm Edit")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Confirm")
            }
        }
    }
}
