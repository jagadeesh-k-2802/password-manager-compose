package com.jackappsdev.password_manager.presentation.screens.edit_password_item.components

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
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.components.ColoredCircle
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemState
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.event.EditPasswordItemUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    navController: NavController,
    state: EditPasswordItemState,
    categoryItems: State<List<CategoryModel>>,
    onEvent: (EditPasswordItemUiEvent) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = state.isCategoryDropdownVisible,
        onExpandedChange = { onEvent(EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility) }
    ) {
        OutlinedTextField(
            leadingIcon = {
                if (state.category?.id == null) {
                    Icon(imageVector = Icons.Outlined.Block, contentDescription = null)
                } else {
                    ColoredCircle(color = state.category.color)
                }
            },
            value = state.category?.name ?: "",
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
            onDismissRequest = { onEvent(EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility) },
            modifier = Modifier.requiredSizeIn(maxHeight = 150.dp)
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Block,
                        contentDescription = stringResource(R.string.text_no_category)
                    )
                },
                text = { Text(text = stringResource(R.string.text_no_category)) },
                onClick = {
                    onEvent(EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility)
                    onEvent(EditPasswordItemUiEvent.OnSelectCategory(null))
                }
            )

            categoryItems.value.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = { ColoredCircle(color = item.color) },
                    text = { Text(text = item.name) },
                    onClick = {
                        onEvent(EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility)
                        onEvent(EditPasswordItemUiEvent.OnSelectCategory(item))
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
                    navController.navigate(Routes.AddCategoryItem)
                    onEvent(EditPasswordItemUiEvent.ToggleCategoryDropdownVisibility)
                }
            )
        }
    }
}
