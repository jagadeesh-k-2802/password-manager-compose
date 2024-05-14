package com.jackappsdev.password_manager.presentation.screens.password_item_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PasswordItemDetailScreen() {
    val scrollState = rememberScrollState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(pagePadding)
                .verticalScroll(scrollState)
                .rotaryWithScroll(scrollState),
        ) {
            ListHeader {
                Text(text = "Amazon", textAlign = TextAlign.Center)
            }

            Card(
                onClick = {},
                enabled = false,
                backgroundPainter = CardDefaults.cardBackgroundPainter(
                    startBackgroundColor = MaterialTheme.colors.surface,
                    endBackgroundColor = MaterialTheme.colors.surface,
                )
            ) {
                Text("jack@mail.com")
                Spacer(modifier = Modifier.height(4.dp))
                Text("123456")
            }
        }
    }
}
