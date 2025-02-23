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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.screens.android_watch.composables.DisableAndroidWatchDialog
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowinsetsVerticalZero
import com.jackappsdev.password_manager.shared.constants.KEY_PIN
import com.jackappsdev.password_manager.shared.constants.SET_PIN_PATH
import com.jackappsdev.password_manager.shared.constants.VERIFY_WEAR_APP
import com.jackappsdev.password_manager.shared.constants.WIPE_DATA_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndroidWatchScreen(
    navController: NavController,
    viewModel: AndroidWatchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var pin by rememberSaveable { mutableStateOf("") }
    var showPin by rememberSaveable { mutableStateOf(false) }
    var showDisableAndroidWatchDialog by rememberSaveable { mutableStateOf(false) }
    val error by viewModel.errorChannel.receiveAsFlow().collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showDisableAndroidWatchDialog) DisableAndroidWatchDialog(
        onConfirm = {
            viewModel.setUseAndroidWatch(false)
            showDisableAndroidWatchDialog = false

            viewModel.setAndroidWatchPin(null) {
                val dataClient = Wearable.getDataClient(context)

                val putDataRequest = PutDataMapRequest.create(WIPE_DATA_PATH).run {
                    dataMap.putString(KEY_PIN, System.currentTimeMillis().toString())
                    setUrgent()
                    asPutDataRequest()
                }

                dataClient.putDataItem(putDataRequest).addOnCompleteListener {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_android_watch_disabled),
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigateUp()
                }
            }
        },
        onDismiss = { showDisableAndroidWatchDialog = false }
    )

    val checkIfWatchIsAvailableAndAppInstalled = remember<(onComplete: () -> Unit) -> Unit> {
        { onComplete ->
            var isWatchConnectedAndAppInstalled = false
            val nodeClient = Wearable.getNodeClient(context)
            val capabilityClient = Wearable.getCapabilityClient(context)

            // Check if Wear OS Watch is connected & app is installed
            scope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val nodes = nodeClient.connectedNodes.await()

                        if (nodes.isNotEmpty()) {
                            val capabilityInfo = capabilityClient.getCapability(
                                VERIFY_WEAR_APP,
                                CapabilityClient.FILTER_REACHABLE
                            ).await()

                            if (capabilityInfo.nodes.isNotEmpty()) {
                                isWatchConnectedAndAppInstalled = true
                            }
                        }
                    } catch (e: Exception) {
                        // Do Nothing
                    }
                }
            }.invokeOnCompletion {
                if (!isWatchConnectedAndAppInstalled) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_connect_watch),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onComplete()
                }
            }
        }
    }

    val onPinChange = remember {
        {
            keyboardController?.hide()
            val dataClient = Wearable.getDataClient(context)

            checkIfWatchIsAvailableAndAppInstalled {
                viewModel.setAndroidWatchPin(pin) {
                    val putDataRequest = PutDataMapRequest.create(SET_PIN_PATH).run {
                        // Adding time part of data to allow setting the same PIN within
                        // short span of time
                        dataMap.putString(KEY_PIN, "$pin ${System.currentTimeMillis()}")
                        setUrgent()
                        asPutDataRequest()
                    }

                    dataClient.putDataItem(putDataRequest).addOnSuccessListener {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_watch_pin_set),
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.navigateUp()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.title_android_watch)) },
                windowInsets = windowinsetsVerticalZero
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
                        onCheckedChange = { value ->
                            checkIfWatchIsAvailableAndAppInstalled {
                                if (value) viewModel.setUseAndroidWatch(true)
                                else showDisableAndroidWatchDialog = true
                            }
                        }
                    )
                },
                modifier = Modifier.clickable {
                    checkIfWatchIsAvailableAndAppInstalled {
                        if (state.useAndroidWatch == false) viewModel.setUseAndroidWatch(true)
                        else showDisableAndroidWatchDialog = true
                    }
                }
            )

            if (state.useAndroidWatch == true) Column {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { value -> if (value.length <= 4) pin = value },
                    label = {
                        Text(
                            stringResource(
                                if (state.hasAndroidWatchPinSet != true) R.string.label_enter_pin
                                else R.string.label_update_pin
                            )
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = pagePadding)
                        .fillMaxWidth(),
                    isError = error is AndroidWatchError.PinError,
                    singleLine = true,
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
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onPinChange() }
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onPinChange() },
                    modifier = Modifier
                        .padding(horizontal = pagePadding)
                        .fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.Done,
                        stringResource(
                            if (state.hasAndroidWatchPinSet != true) R.string.accessibility_set_watch_pin
                            else R.string.accessibility_update_watch_pin
                        )
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        stringResource(
                            if (state.hasAndroidWatchPinSet != true) R.string.btn_set_pin
                            else R.string.btn_update_pin
                        )
                    )
                }
            }
        }
    }
}
