package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.jackappsdev.password_manager.presentation.theme.pagePadding

@Composable
fun PasswordLockScreen() {
    // TODO: Show Enable "Android Wear Support" When PIN Not Available

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(pagePadding)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "••••",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                letterSpacing = TextUnit(2.5f, TextUnitType.Sp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("1") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("2") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("3") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { },
                    contentPadding = PaddingValues(8.dp),
                    icon = {
                        Icon(Icons.AutoMirrored.Default.Backspace, contentDescription = "Delete")
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("4") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("5") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("6") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { },
                    contentPadding = PaddingValues(8.dp),
                    icon = {
                        Icon(Icons.Default.Done, contentDescription = "Done")
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("7") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("8") }
                )

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("9") }
                )

                Spacer(modifier = Modifier.size(30.dp))
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.size(30.dp))

                Chip(
                    onClick = {},
                    modifier = Modifier.size(30.dp),
                    colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                    label = { Text("0") }
                )

                Spacer(modifier = Modifier.size(70.dp))
            }
        }
    }
}