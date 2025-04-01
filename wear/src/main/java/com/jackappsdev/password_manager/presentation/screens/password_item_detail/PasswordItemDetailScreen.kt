package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.components.ItemView

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PasswordItemDetailScreen(
    navController: NavController,
    passwordItem: State<PasswordItemModel?>
) {
    var isValueAlreadySetOnce by rememberSaveable { mutableStateOf(false) }

    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Icon,
            last = ScalingLazyColumnDefaults.ItemType.Card
        )
    )

    LaunchedEffect(passwordItem) {
        if (passwordItem.value == null && isValueAlreadySetOnce) {
            navController.navigateUp()
        } else {
            isValueAlreadySetOnce = true
        }
    }

    ScreenScaffold(scrollState = columnState) {
        ItemView(passwordItem.value, columnState)
    }
}
