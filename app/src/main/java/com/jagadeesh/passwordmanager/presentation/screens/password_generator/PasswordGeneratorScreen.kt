package com.jagadeesh.passwordmanager.presentation.screens.password_generator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jagadeesh.passwordmanager.presentation.theme.pagePadding
import com.jagadeesh.passwordmanager.presentation.utils.copyToClipboard
import com.jagadeesh.passwordmanager.presentation.utils.generateRandomPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(navController: NavController) {
    var generatedPassword by rememberSaveable { mutableStateOf("") }
    var lengthValue by rememberSaveable { mutableIntStateOf(8) }
    var includeLowercase by rememberSaveable { mutableStateOf(true) }
    var includeUppercase by rememberSaveable { mutableStateOf(true) }
    var includeNumbers by rememberSaveable { mutableStateOf(true) }
    var includeSymbols by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current

    // Generate new password whenever one of the arg changes
    LaunchedEffect(
        lengthValue,
        includeLowercase,
        includeUppercase,
        includeNumbers,
        includeSymbols
    ) {
        generatedPassword = generatePassword(
            lengthValue,
            includeLowercase,
            includeUppercase,
            includeNumbers,
            includeSymbols
        )
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Password Generator") }) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
        ) {
            SelectionContainer {
                Column(
                    modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
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
                            when (lengthValue) {
                                in (1..6) -> "Weak"
                                in (6..16) -> "Medium"
                                else -> "Strong"
                            }
                        )

                        Row {
                            IconButton(onClick = {
                                generatedPassword = generatePassword(
                                    lengthValue,
                                    includeLowercase,
                                    includeUppercase,
                                    includeNumbers,
                                    includeSymbols
                                )
                            }) {
                                Icon(
                                    Icons.Filled.Refresh,
                                    contentDescription = "Generate again"
                                )
                            }

                            IconButton(onClick = { copyToClipboard(context, generatedPassword) }) {
                                Icon(
                                    Icons.Filled.ContentCopy,
                                    contentDescription = "Copy password"
                                )
                            }
                        }
                    }
                }
            }

            Column {
                Text("Length: $lengthValue")

                Slider(
                    value = lengthValue.toFloat(),
                    valueRange = 6f..32f,
                    onValueChange = { lengthValue = it.toInt() },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OptionSwitch(
                title = "Include Lowercase (abc)",
                isItemChecked = includeLowercase,
                onItemCheckChange = { value -> includeLowercase = value }
            )

            OptionSwitch(
                title = "Include Uppercase (ABC)",
                isItemChecked = includeUppercase,
                onItemCheckChange = { value -> includeUppercase = value }
            )

            OptionSwitch(
                title = "Include Numbers (123)",
                isItemChecked = includeNumbers,
                onItemCheckChange = { value -> includeNumbers = value }
            )

            OptionSwitch(
                title = "Include Symbols (!$@)",
                isItemChecked = includeSymbols,
                onItemCheckChange = { value -> includeSymbols = value }
            )
        }
    }
}

fun generatePassword(
    lengthValue: Int,
    includeLowercase: Boolean,
    includeUppercase: Boolean,
    includeNumbers: Boolean,
    includeSymbols: Boolean
) = generateRandomPassword(
    lengthValue,
    includeLowercase,
    includeUppercase,
    includeNumbers,
    includeSymbols
)
