package com.jackappsdev.password_manager.presentation.screens.password_lock

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.Wearable
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import com.jackappsdev.password_manager.presentation.theme.isLargeDisplay
import com.jackappsdev.password_manager.shared.constants.PLAY_STORE_APP_URI
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun PasswordLockScreen(
    viewModel: PasswordLockViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var pin by rememberSaveable { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val numButtonPadding = if (isLargeDisplay()) 20.dp else 18.dp
    val outerScreenPadding = if (isLargeDisplay()) 16.dp else 12.dp

    val onNumberButtonClick = remember {
        { character: String -> if (pin.length < 4) pin += character }
    }

    val onBackSpace = remember {
        { if (pin.isNotEmpty()) pin = pin.substring(0, pin.length - 1) }
    }

    val onDone = remember {
        {
            scope.launch {
                if (viewModel.verifyPin(pin)) {
                    viewModel.setUnlocked(true)
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                } else {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    pin = ""

                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_incorrect_password),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    if (state.hasPinSet != true) {
        ScreenScaffold(
            modifier = Modifier.fillMaxSize(),
            positionIndicator = { PositionIndicator(scrollState = scrollState) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (isLargeDisplay()) Arrangement.Center else Arrangement.Top,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .rotaryScrollable(
                        RotaryScrollableDefaults.behavior(scrollState),
                        rememberActiveFocusRequester()
                    )
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    Icons.Default.WarningAmber,
                    contentDescription = stringResource(R.string.accessibility_warning_icon),
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.text_enable_watch_support),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                )

                Spacer(modifier = Modifier.height(8.dp))

                CompactChip(
                    onClick = {
                        val remoteActivityHelper = RemoteActivityHelper(
                            context,
                            Executors.newSingleThreadExecutor()
                        )

                        val intent = Intent(Intent.ACTION_VIEW)
                            .addCategory(Intent.CATEGORY_BROWSABLE)
                            .setData(PLAY_STORE_APP_URI.toUri())

                        scope.launch {
                            try {
                                val nodeClient = Wearable.getNodeClient(context)
                                val node = nodeClient.connectedNodes.await().first()
                                remoteActivityHelper.startRemoteActivity(intent, node.id)

                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_opened_phone_app),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (exception: Exception) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_cannot_open_phone_app),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.btn_open_app),
                            style = MaterialTheme.typography.button
                        )
                    },
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )
            }
        }
    } else {
        ScreenScaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(outerScreenPadding)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    stringResource(R.string.text_password_dot).repeat(pin.length),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = TextUnit(5f, TextUnitType.Sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_one)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_one),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_two)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_two),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_three)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_three),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onBackSpace() },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        label = { },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Default.Backspace,
                                modifier = Modifier.size(16.dp),
                                contentDescription = stringResource(R.string.accessibility_backspace)
                            )
                        }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_four)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_four),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_five)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_five),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_six)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_six),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onDone() },
                        enabled = pin.length == 4,
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        label = { },
                        icon = {
                            Icon(
                                Icons.Default.CheckCircle,
                                modifier = Modifier.size(18.dp),
                                contentDescription = stringResource(R.string.accessibility_done)
                            )
                        }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_seven)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_seven),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_eight)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_eight),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_nine)) },
                        modifier = Modifier.weight(0.5f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_nine),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Spacer(modifier = Modifier.weight(0.5f))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Chip(
                        onClick = { onNumberButtonClick(context.getString(R.string.btn_zero)) },
                        modifier = Modifier.weight(1f),
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = numButtonPadding),
                        label = {
                            Text(
                                stringResource(R.string.btn_zero),
                                style = MaterialTheme.typography.title2
                            )
                        }
                    )

                    Spacer(modifier = Modifier.weight(2f))
                }
            }
        }
    }
}

@WearPreviewSmallRound
@WearPreviewLargeRound
@WearPreviewFontScales
@Composable
fun PasswordLockScreenPreview() {
    PasswordManagerTheme { PasswordLockScreen() }
}
