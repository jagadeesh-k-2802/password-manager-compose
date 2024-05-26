package com.jackappsdev.password_manager.presentation.screens.android_watch

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.shared.constants.KEY_PIN
import com.jackappsdev.password_manager.shared.constants.SET_PIN_PATH
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndroidWatchScreen(
    navController: NavController,
    viewModel: AndroidWatchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var pin by rememberSaveable { mutableStateOf("") }
    var showPin by rememberSaveable { mutableStateOf(false) }
    val error by viewModel.errorChannel.receiveAsFlow().collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_android_watch)) }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.label_enable_android_watch)) },
                supportingContent = { Text(stringResource(R.string.text_android_watch_note)) },
                trailingContent = {
                    Switch(
                        checked = state.useAndroidWatch == true,
                        onCheckedChange = { value -> viewModel.setUseAndroidWatch(value) }
                    )
                },
                modifier = Modifier.clickable { viewModel.setUseAndroidWatch(state.useAndroidWatch != true) }
            )

            if (state.useAndroidWatch == true) Column {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { value -> if (value.length <= 4) pin = value },
                    label = { Text(stringResource(R.string.label_enter_pin)) },
                    modifier = Modifier
                        .padding(horizontal = pagePadding)
                        .fillMaxWidth(),
                    isError = error is AndroidWatchError.PinError,
                    supportingText = {
                        error?.let {
                            if (it is AndroidWatchError.PinError) Text(stringResource(it.error))
                        }
                    },
                    visualTransformation = if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPin = !showPin }) {
                            Icon(
                                if (showPin) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.setAndroidWatchPin(pin) {
                            val dataClient = Wearable.getDataClient(context)

                            val putDataRequest = PutDataMapRequest.create(SET_PIN_PATH).run {
                                dataMap.putString(KEY_PIN, pin)
                                setUrgent()
                                asPutDataRequest()
                            }

                            dataClient.putDataItem(putDataRequest).addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_watch_pin_set),
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = pagePadding)
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_set_pin))
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.btn_set_pin))
                }
            }
        }
    }
}
