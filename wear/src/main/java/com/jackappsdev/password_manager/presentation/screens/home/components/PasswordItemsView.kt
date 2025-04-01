package com.jackappsdev.password_manager.presentation.screens.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.presentation.navigation.Routes

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PasswordItemsView(
    navController: NavController,
    passwordItems: List<PasswordItemModel>?,
    columnState: ScalingLazyColumnState
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        columnState = columnState
    ) {
        item {
            ListHeader {
                Text(stringResource(R.string.title_passwords))
            }
        }

        passwordItems?.let { passwords ->
            items(passwords) { item ->
                PasswordItem(label = item.name) {
                    navController.navigate(Routes.PasswordItemDetail(item.id ?: 0))
                }
            }
        }
    }
}
