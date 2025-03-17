package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.components.DetailContentView

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PasswordItemDetailScreen(
    navController: NavController,
    viewModel: PasswordItemDetailViewModel = hiltViewModel()
) {
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)
    var isValueAlreadySetOnce by rememberSaveable { mutableStateOf(false) }

    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Icon,
            last = ScalingLazyColumnDefaults.ItemType.Card
        )
    )

    LaunchedEffect(passwordItem) {
        if (passwordItem == null && isValueAlreadySetOnce) {
            navController.navigateUp()
        } else {
            isValueAlreadySetOnce = true
        }
    }

    ScreenScaffold(scrollState = columnState) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            columnState = columnState
        ) {
            item {
                ListHeader { Text(text = passwordItem?.name ?: "...") }
            }

            item {
                DetailContentView {
                    Text(stringResource(R.string.label_username), fontWeight = FontWeight.Bold)
                    Text("${passwordItem?.username}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(stringResource(R.string.label_password), fontWeight = FontWeight.Bold)
                    Text("${passwordItem?.password}")
                }
            }

            if (passwordItem?.notes?.isNotBlank() == true) {
                item {
                    DetailContentView {
                        Text(stringResource(R.string.label_notes), fontWeight = FontWeight.Bold)
                        Text(passwordItem?.notes ?: "")
                    }
                }
            }
        }
    }
}
