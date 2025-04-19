package com.jackappsdev.password_manager.presentation.screens.add_password_item.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.components.ColoredCircle
import com.jackappsdev.password_manager.presentation.screens.add_password_item.AddPasswordItemState
import com.jackappsdev.password_manager.presentation.screens.add_password_item.event.AddPasswordItemUiEvent
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    state: AddPasswordItemState,
    categoryItems: State<List<CategoryModel>>,
    onEvent: (AddPasswordItemUiEvent) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = state.isCategoryDropdownVisible,
        onExpandedChange = { onEvent(AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility) }
    ) {
        OutlinedTextField(
            leadingIcon = {
                if (state.category?.name == stringResource(R.string.text_no_category)) {
                    Icon(imageVector = Icons.Outlined.Block, contentDescription = null)
                } else {
                    ColoredCircle(color = state.category?.color ?: EMPTY_STRING)
                }
            },
            value = state.category?.name ?: EMPTY_STRING,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_category)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = state.isCategoryDropdownVisible
                )
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = state.isCategoryDropdownVisible,
            onDismissRequest = { onEvent(AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility) },
            modifier = Modifier.requiredSizeIn(maxHeight = 150.dp)
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Block,
                        contentDescription = stringResource(R.string.text_no_category)
                    )
                },
                text = { Text(stringResource(R.string.text_no_category)) },
                onClick = {
                    onEvent(AddPasswordItemUiEvent.SelectCategory(null))
                    onEvent(AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility)
                }
            )

            categoryItems.value.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = { ColoredCircle(color = item.color) },
                    text = { Text(text = item.name) },
                    onClick = {
                        onEvent(AddPasswordItemUiEvent.SelectCategory(item))
                        onEvent(AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility)
                    }
                )
            }

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.accessibility_add_category)
                    )
                },
                text = { Text(text = stringResource(R.string.label_create_new_category)) },
                onClick = {
                    onEvent(AddPasswordItemUiEvent.NavigateToAddCategory)
                    onEvent(AddPasswordItemUiEvent.ToggleCategoryDropdownVisibility)
                }
            )
        }
    }
}
