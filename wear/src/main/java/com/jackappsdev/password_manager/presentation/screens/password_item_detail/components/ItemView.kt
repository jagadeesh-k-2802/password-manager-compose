package com.jackappsdev.password_manager.presentation.screens.password_item_detail.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.PasswordItemModel

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun ItemView(
    passwordItem: PasswordItemModel?,
    columnState: ScalingLazyColumnState
) {
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
                    Text(passwordItem.notes)
                }
            }
        }
    }
}
