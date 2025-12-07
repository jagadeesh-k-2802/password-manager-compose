package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.parseModifiedTime
import com.jackappsdev.password_manager.presentation.components.ColoredCircle
import com.jackappsdev.password_manager.presentation.components.ConfirmationDialog
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.components.PasswordItemDetailActions
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailEffectHandler
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEffect
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent
import com.jackappsdev.password_manager.presentation.theme.disabledButEnabledOutlinedTextFieldColors
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordItemDetailScreen(
    state: PasswordItemDetailState,
    effectFlow: Flow<PasswordItemDetailUiEffect>,
    effectHandler: PasswordItemDetailEffectHandler,
    onEvent: (PasswordItemDetailUiEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val passwordItem = state.passwordItem?.collectAsState()?.value

    LaunchedEffect(key1 = passwordItem) {
        with(effectHandler) {
            effectFlow.collectLatest { effect ->
                when (effect) {
                    is PasswordItemDetailUiEffect.CopyText -> onCopy(effect.text)
                    is PasswordItemDetailUiEffect.LaunchUrl -> onLaunchUrl(effect.url)
                    is PasswordItemDetailUiEffect.ToggleAddToWatch -> onToggleAddedToWatch(passwordItem)
                    is PasswordItemDetailUiEffect.DeleteItem -> onDeleteItem(passwordItem)
                    is PasswordItemDetailUiEffect.NavigateToEditPassword -> onNavigateToEditPassword(effect.id)
                    is PasswordItemDetailUiEffect.NavigateUp -> onNavigateUp()
                }
            }
        }
    }

    if (state.isDeleteDialogVisible) {
        ConfirmationDialog(
            title = R.string.dialog_title_delete_password,
            description = R.string.dialog_text_password_delete,
            onConfirm = { onEvent(PasswordItemDetailUiEvent.RequestDeleteItem) },
            onDismiss = { onEvent(PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        passwordItem?.name ?: EMPTY_STRING,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PasswordItemDetailUiEvent.NavigateUp) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                actions = {
                    PasswordItemDetailActions(
                        state = state,
                        passwordItem = passwordItem,
                        onEvent = onEvent
                    )
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
                value = passwordItem?.name ?: EMPTY_STRING,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Badge, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_name)) },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordItem?.username ?: EMPTY_STRING,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_username)) },
                trailingIcon = {
                    if ((passwordItem?.username?.length ?: 0) > 0) {
                        IconButton(onClick = {
                            onEvent(PasswordItemDetailUiEvent.CopyText(passwordItem?.username))
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = stringResource(R.string.accessibility_copy_text)
                            )
                        }
                    }
                },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordItem?.password ?: EMPTY_STRING,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Key, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_password)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                visualTransformation = if (state.showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    if ((passwordItem?.password?.length ?: 0) > 0) {
                        Row {
                            IconButton(onClick = { onEvent(PasswordItemDetailUiEvent.ToggleShowPasswordVisibility) }) {
                                Icon(
                                    imageVector = if (state.showPassword) {
                                        Icons.Outlined.VisibilityOff
                                    } else {
                                        Icons.Outlined.Visibility
                                    },
                                    contentDescription = stringResource(R.string.accessibility_toggle_password)
                                )
                            }

                            IconButton(onClick = {
                                onEvent(PasswordItemDetailUiEvent.CopyText(passwordItem?.password))
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = stringResource(R.string.accessibility_copy_text)
                                )
                            }
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordItem?.website ?: EMPTY_STRING,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Link, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_website)) },
                maxLines = 1,
                trailingIcon = {
                    if ((passwordItem?.website?.length ?: 0) > 0) {
                        IconButton(onClick = {
                            onEvent(PasswordItemDetailUiEvent.LaunchUrl(passwordItem?.website ?: EMPTY_STRING))
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                                contentDescription = stringResource(R.string.accessibility_open_website)
                            )
                        }
                    }
                },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordItem?.notes ?: EMPTY_STRING,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_notes)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                trailingIcon = {
                    if ((passwordItem?.notes?.length ?: 0) > 0) {
                        IconButton(onClick = {
                            onEvent(PasswordItemDetailUiEvent.CopyText(passwordItem?.notes))
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = stringResource(R.string.accessibility_copy_text)
                            )
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
                OutlinedTextField(
                    leadingIcon = {
                        if (passwordItem?.categoryId == null) {
                            Icon(Icons.Outlined.Block, null)
                        } else {
                            ColoredCircle(color = passwordItem.categoryColor ?: EMPTY_STRING)
                        }
                    },
                    value = passwordItem?.categoryName ?: stringResource(R.string.text_no_category),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_category)) },
                    enabled = false,
                    colors = disabledButEnabledOutlinedTextFieldColors(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = false, onDismissRequest = {}) {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (passwordItem?.createdAt != null) {
                    parseModifiedTime(context, passwordItem.createdAt)
                } else {
                    EMPTY_STRING
                },
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.AccessTime, contentDescription = null)
                },
                label = { Text(stringResource(R.string.label_last_updated_at)) },
                enabled = false,
                colors = disabledButEnabledOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
