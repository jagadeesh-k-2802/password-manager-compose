package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import android.text.format.DateFormat
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material.icons.outlined.WatchOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.copyToClipboard
import com.jackappsdev.password_manager.core.launchUrl
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemDto
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.theme.disabledButEnabledOutlinedTextFieldColors
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.shared.constants.DELETE_PASSWORD
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordItemDetailScreen(
    navController: NavController,
    viewModel: PasswordItemDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var dropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogVisible by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if (isDeleteDialogVisible) PasswordItemDeleteDialog(
        onConfirm = {
            passwordItem?.let { passwordCategoryModel ->
                isDeleteDialogVisible = false
                val dataClient = Wearable.getDataClient(context)

                val putDataRequest = PutDataMapRequest.create(DELETE_PASSWORD).run {
                    dataMap.putString(
                        KEY_PASSWORD,
                        Json.encodeToString(passwordCategoryModel.toPasswordItemDto())
                    )
                    setUrgent()
                    asPutDataRequest()
                }

                dataClient.putDataItem(putDataRequest).addOnCompleteListener {
                    viewModel.deleteItem(passwordCategoryModel)
                    navController.navigateUp()
                }
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
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { dropDownMenuExpanded = true }) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            stringResource(R.string.accessibility_options)
                        )
                    }

                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = { dropDownMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    stringResource(R.string.accessibility_edit_item)
                                )
                            },
                            text = { Text("Edit") },
                            onClick = {
                                dropDownMenuExpanded = false
                                navController.navigate(
                                    Routes.EditPasswordItem.getPath(
                                        passwordItem?.id ?: 0
                                    )
                                )
                            }
                        )

                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Delete,
                                    stringResource(R.string.accessibility_delete_item)
                                )
                            },
                            text = { Text("Delete") },
                            onClick = {
                                dropDownMenuExpanded = false
                                isDeleteDialogVisible = true
                            }
                        )

                        if (state.hasAndroidWatchPinSet == true) DropdownMenuItem(
                            leadingIcon = {
                                if (passwordItem?.isAddedToWatch != true)
                                    Icon(
                                        Icons.Outlined.Watch,
                                        stringResource(R.string.accessibility_add_to_watch)
                                    )
                                else Icon(
                                    Icons.Outlined.WatchOff,
                                    stringResource(R.string.accessibility_remove_from_watch)
                                )
                            },
                            text = {
                                if (passwordItem?.isAddedToWatch != true) Text("Add to Watch")
                                else Text("Remove from Watch")
                            },
                            onClick = {
                                dropDownMenuExpanded = false
                                val dataClient = Wearable.getDataClient(context)
                                val path = if (passwordItem?.isAddedToWatch != true) UPSERT_PASSWORD
                                else DELETE_PASSWORD

                                val putDataRequest = PutDataMapRequest.create(path).run {
                                    dataMap.putString(
                                        KEY_PASSWORD,
                                        Json.encodeToString(passwordItem?.toPasswordItemDto())
                                    )
                                    setUrgent()
                                    asPutDataRequest()
                                }

                                dataClient.putDataItem(putDataRequest).addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        if (passwordItem?.isAddedToWatch != true) context.getString(
                                            R.string.toast_added_to_watch
                                        )
                                        else context.getString(
                                            R.string.toast_removed_from_watch
                                        ),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    passwordItem?.let { passwordCategoryModel ->
                                        viewModel.toggleIsAddedToWatch(passwordCategoryModel)
                                    }
                                }
                            }
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
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = passwordItem?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_name)) },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.username ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_username)) },
                trailingIcon = {
                    if ((passwordItem?.username?.length ?: 0) > 0) {
                        IconButton(onClick = {
                            copyToClipboard(
                                context,
                                passwordItem?.username
                            )
                        }) {
                            Icon(
                                Icons.Outlined.ContentCopy,
                                stringResource(R.string.accessibility_copy_text)
                            )
                        }
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
                label = { Text(stringResource(R.string.label_password)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if ((passwordItem?.password?.length ?: 0) > 0) {
                        Row {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = stringResource(R.string.accessibility_toggle_password)
                                )
                            }

                            IconButton(onClick = {
                                copyToClipboard(
                                    context,
                                    passwordItem?.password
                                )
                            }) {
                                Icon(
                                    Icons.Outlined.ContentCopy,
                                    stringResource(R.string.accessibility_copy_text)
                                )
                            }
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.website ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_website)) },
                trailingIcon = {
                    if ((passwordItem?.website?.length ?: 0) > 0) {
                        IconButton(onClick = {
                            launchUrl(
                                context,
                                passwordItem?.website ?: ""
                            )
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.OpenInNew,
                                stringResource(R.string.accessibility_open_website)
                            )
                        }
                    }
                },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordItem?.notes ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_notes)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                trailingIcon = {
                    if ((passwordItem?.notes?.length ?: 0) > 0) {
                        IconButton(onClick = {
                            copyToClipboard(
                                context,
                                passwordItem?.notes
                            )
                        }) {
                            Icon(
                                Icons.Outlined.ContentCopy,
                                stringResource(R.string.accessibility_copy_text)
                            )
                        }
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
                                    .background(
                                        parseColor(
                                            passwordItem?.categoryColor ?: ""
                                        )
                                    )
                                    .size(24.dp)
                            ) {}
                        }
                    },
                    value = passwordItem?.categoryName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_category)) },
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
                    val dateFormat =
                        SimpleDateFormat("dd/MM/yyyy $hours:mm", Locale.ENGLISH)
                    dateFormat.format(Date(passwordItem?.createdAt!!))
                } else {
                    ""
                },
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_last_updated_at)) },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
