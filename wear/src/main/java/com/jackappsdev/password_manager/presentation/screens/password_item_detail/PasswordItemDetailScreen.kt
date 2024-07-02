package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.jackappsdev.password_manager.R

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PasswordItemDetailScreen(
    viewModel: PasswordItemDetailViewModel = hiltViewModel()
) {
    val passwordItem by viewModel.passwordItem.collectAsState(initial = null)

    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Icon,
            last = ScalingLazyColumnDefaults.ItemType.Card
        )
    )

    ScreenScaffold(scrollState = columnState) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            columnState = columnState
        ) {
            item {
                ListHeader { Text(text = passwordItem?.name ?: "...") }
            }

            item {
                Card(
                    onClick = {},
                    enabled = false,
                    backgroundPainter = CardDefaults.cardBackgroundPainter(
                        startBackgroundColor = MaterialTheme.colors.surface,
                        endBackgroundColor = MaterialTheme.colors.surface,
                    )
                ) {
                    Text(stringResource(R.string.label_username), fontWeight = FontWeight.Bold)
                    Text("${passwordItem?.username}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.label_password), fontWeight = FontWeight.Bold)
                    Text("${passwordItem?.password}")
                }
            }

            if (passwordItem?.notes?.isNotEmpty() == true)
                item {
                    Card(
                        onClick = {},
                        enabled = false,
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = MaterialTheme.colors.surface,
                            endBackgroundColor = MaterialTheme.colors.surface,
                        )
                    ) {
                        Text(stringResource(R.string.label_notes), fontWeight = FontWeight.Bold)
                        Text(passwordItem?.notes ?: "")
                    }
                }
        }
    }
}
