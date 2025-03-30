package com.jackappsdev.password_manager.presentation.screens.password_generator

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.GeneratePasswordConfig
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.core.copyToClipboard
import com.jackappsdev.password_manager.core.generateRandomPassword
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.presentation.screens.password_generator.composables.OptionSwitch
import com.jackappsdev.password_manager.presentation.theme.windowInsetsVerticalZero
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen() {
    var generatedPassword by rememberSaveable { mutableStateOf("") }
    var lengthValue by rememberSaveable { mutableIntStateOf(8) }
    var includeLowercase by rememberSaveable { mutableStateOf(true) }
    var includeUppercase by rememberSaveable { mutableStateOf(true) }
    var includeNumbers by rememberSaveable { mutableStateOf(true) }
    var includeSymbols by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Generate new password whenever one of the arg changes
    LaunchedEffect(
        lengthValue,
        includeLowercase,
        includeUppercase,
        includeNumbers,
        includeSymbols
    ) {
        generatedPassword = generateRandomPassword(
            GeneratePasswordConfig(
                lengthValue,
                includeLowercase,
                includeUppercase,
                includeNumbers,
                includeSymbols
            )
        )
    }

    val isLastOne: (Boolean) -> Boolean = remember {
        { value ->
            var count = 0
            if (!includeLowercase) count++
            if (!includeUppercase) count++
            if (!includeNumbers) count++
            if (!includeSymbols) count++

            if (count == 3 && !value) {
                scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.text_all_options_warning)) }
                false
            } else {
                true
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_password_generator)) },
                windowInsets = windowInsetsVerticalZero
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                            bottom = 20.dp,
                            start = pagePadding,
                            end = pagePadding
                        )
                ) {
                    Text(
                        text = generatedPassword,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (lengthValue) {
                                in (1..8) -> stringResource(R.string.text_weak)
                                in (8..12) -> stringResource(R.string.text_medium)
                                else -> stringResource(R.string.text_strong)
                            },
                            color = if (isSystemInDarkTheme()) {
                                when (lengthValue) {
                                    in (1..8) -> parseColor("#FF8C69")
                                    in (8..12) -> parseColor("#FFD700")
                                    else -> parseColor("#32CD32")
                                }
                            } else {
                                when (lengthValue) {
                                    in (1..8) -> parseColor("#A61B11")
                                    in (8..12) -> parseColor("#D1A000")
                                    else -> parseColor("#3D9050")
                                }
                            },
                            fontWeight = FontWeight.SemiBold
                        )

                        Row {
                            IconButton(onClick = {
                                generatedPassword = generateRandomPassword(
                                    GeneratePasswordConfig(
                                        lengthValue,
                                        includeLowercase,
                                        includeUppercase,
                                        includeNumbers,
                                        includeSymbols
                                    )
                                )
                            }) {
                                Icon(
                                    Icons.Outlined.Refresh,
                                    contentDescription = stringResource(R.string.accessibility_generate_again)
                                )
                            }

                            IconButton(onClick = { copyToClipboard(context, generatedPassword) }) {
                                Icon(
                                    Icons.Outlined.ContentCopy,
                                    contentDescription = stringResource(R.string.accessibility_copy_text)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = pagePadding)
            ) {
                Text(stringResource(R.string.text_length, lengthValue))

                Slider(
                    value = lengthValue.toFloat(),
                    valueRange = 6f..32f,
                    onValueChange = { lengthValue = it.toInt() },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OptionSwitch(
                title = stringResource(R.string.label_include_lowercase),
                isItemChecked = includeLowercase,
                onItemCheckChange = { value -> if (isLastOne(value)) includeLowercase = value }
            )

            OptionSwitch(
                title = stringResource(R.string.label_include_uppercase),
                isItemChecked = includeUppercase,
                onItemCheckChange = { value -> if (isLastOne(value)) includeUppercase = value }
            )

            OptionSwitch(
                title = stringResource(R.string.label_include_numbers),
                isItemChecked = includeNumbers,
                onItemCheckChange = { value -> if (isLastOne(value)) includeNumbers = value }
            )

            OptionSwitch(
                title = stringResource(R.string.label_include_symbols),
                isItemChecked = includeSymbols,
                onItemCheckChange = { value -> if (isLastOne(value)) includeSymbols = value }
            )
        }
    }
}
