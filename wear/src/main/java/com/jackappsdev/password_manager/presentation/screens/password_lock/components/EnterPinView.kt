package com.jackappsdev.password_manager.presentation.screens.password_lock.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.base.WearPreview
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockState
import com.jackappsdev.password_manager.presentation.screens.password_lock.event.PasswordLockUiEvent
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import com.jackappsdev.password_manager.presentation.theme.isLargeDisplay

@Composable
fun EnterPinView(
    state: PasswordLockState,
    onEvent: (PasswordLockUiEvent) -> Unit
) {
    val context = LocalContext.current
    val outerScreenPadding = if (isLargeDisplay()) 16.dp else 12.dp

    ScreenScaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(outerScreenPadding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                stringResource(R.string.text_password_dot).repeat(state.pin.length),
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
                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_one),
                    onEvent = onEvent
                )

                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_two),
                    onEvent = onEvent
                )

                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_three),
                    onEvent = onEvent
                )

                BackSpaceChip(
                    modifier = Modifier.weight(0.5f),
                    onEvent = onEvent
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_four),
                    onEvent = onEvent
                )

                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_five),
                    onEvent = onEvent
                )

                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_six),
                    onEvent = onEvent
                )

                DoneChip(
                    modifier = Modifier.weight(0.5f),
                    enabled = state.pin.length == 4,
                    onEvent = onEvent
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_seven),
                    onEvent = onEvent
                )

                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_eight),
                    onEvent = onEvent
                )

                NumberChip(
                    modifier = Modifier.weight(0.5f),
                    label = context.getString(R.string.btn_nine),
                    onEvent = onEvent
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

                NumberChip(
                    modifier = Modifier.weight(1f),
                    label = context.getString(R.string.btn_zero),
                    onEvent = onEvent
                )

                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
}

@WearPreview
@Composable
private fun EnterPinViewPreview() {
    PasswordManagerTheme {
        val state = PasswordLockState(pin = "1234")
        val onEvent: (PasswordLockUiEvent) -> Unit = {}
        EnterPinView(state, onEvent)
    }
}
