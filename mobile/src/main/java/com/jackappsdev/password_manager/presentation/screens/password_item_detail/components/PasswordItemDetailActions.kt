package com.jackappsdev.password_manager.presentation.screens.password_item_detail.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material.icons.outlined.WatchOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailState
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.event.PasswordItemDetailUiEvent

@Composable
fun RowScope.PasswordItemDetailActions(
    state: PasswordItemDetailState,
    passwordItem: PasswordWithCategoryModel?,
    onEvent: (PasswordItemDetailUiEvent) -> Unit,
) {
    IconButton(onClick = { onEvent(PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility) }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = stringResource(R.string.accessibility_options)
        )
    }

    DropdownMenu(
        expanded = state.dropDownMenuExpanded,
        onDismissRequest = { onEvent(PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility) }
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.accessibility_edit_item)
                )
            },
            text = { Text(stringResource(R.string.btn_edit)) },
            onClick = {
                onEvent(PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility)
                onEvent(PasswordItemDetailUiEvent.NavigateToEditPassword(passwordItem?.id ?: 0))
            }
        )

        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.accessibility_delete_item)
                )
            },
            text = { Text(stringResource(R.string.btn_delete)) },
            onClick = {
                onEvent(PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility)
                onEvent(PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility)
            }
        )

        if (state.hasAndroidWatchPinSet == true) {
            DropdownMenuItem(
                leadingIcon = {
                    if (passwordItem?.isAddedToWatch != true) {
                        Icon(
                            imageVector = Icons.Outlined.Watch,
                            contentDescription = stringResource(R.string.accessibility_add_to_watch)
                        )
                    }
                    else {
                        Icon(
                            imageVector = Icons.Outlined.WatchOff,
                            contentDescription = stringResource(R.string.accessibility_remove_from_watch)
                        )
                    }
                },
                text = {
                    if (passwordItem?.isAddedToWatch != true) {
                        Text(stringResource(R.string.btn_add_to_watch))
                    } else {
                        Text(stringResource(R.string.btn_remove_from_watch))
                    }
                },
                onClick = {
                    onEvent(PasswordItemDetailUiEvent.ToggleDropDownMenuVisibility)
                    onEvent(PasswordItemDetailUiEvent.ToggleAddedToWatch)
                }
            )
        }
    }
}
