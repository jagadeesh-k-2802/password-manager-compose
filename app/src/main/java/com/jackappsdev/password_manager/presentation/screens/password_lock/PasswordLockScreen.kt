package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordLockScreen(
    viewModel: PasswordLockViewModel = hiltViewModel()
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var passwordValue by rememberSaveable { mutableStateOf("") }
    var confirmPasswordValue by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current as FragmentActivity
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val error by viewModel.errorChannel.receiveAsFlow().collectAsState(initial = null)

    val isScreenLockAvailable = remember {
        val manager = BiometricManager.from(context)
        val credentials = BIOMETRIC_WEAK or BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        manager.canAuthenticate(credentials) == BiometricManager.BIOMETRIC_SUCCESS
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(context, R.string.title_biometric))
            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
            .build()
    }

    val biometricPrompt = remember {
        BiometricPrompt(
            context,
            context.mainExecutor,
            object :
                BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.setUnlocked()
                }
            }
        )
    }

    LaunchedEffect(state.hasPasswordSet, state.useScreenLockToUnlock) {
        if (state.hasPasswordSet == true && state.useScreenLockToUnlock == true) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
        ) {
            if (state.hasPasswordSet == true) {
                CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_enter_password)) })
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { value -> passwordValue = value },
                    label = { Text(stringResource(R.string.label_password)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error is PasswordLockError.PasswordError,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    supportingText = {
                        error?.let {
                            if (it is PasswordLockError.PasswordError) Text(stringResource(it.error))
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        scope.launch {
                            if (viewModel.verifyPassword(passwordValue)) {
                                keyboardController?.hide()
                                viewModel.setUnlocked()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.Done,
                        stringResource(R.string.accessibility_confirm)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.btn_confirm))
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (state.useScreenLockToUnlock == true && isScreenLockAvailable) OutlinedButton(
                    onClick = { biometricPrompt.authenticate(promptInfo) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.LockOpen,
                        stringResource(R.string.accessibility_use_device_lock_screen)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.btn_use_device_lock_screen))
                }
            } else {
                CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_create_password)) })
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { value -> passwordValue = value },
                    label = { Text(stringResource(R.string.label_password)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error is PasswordLockError.PasswordError,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    supportingText = {
                        error?.let {
                            if (it is PasswordLockError.PasswordError) Text(stringResource(it.error))
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = stringResource(R.string.accessibility_toggle_password)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )

                OutlinedTextField(
                    value = confirmPasswordValue,
                    onValueChange = { value -> confirmPasswordValue = value },
                    label = { Text(stringResource(R.string.label_confirm_password)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error is PasswordLockError.ConfirmPasswordError,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    supportingText = {
                        error?.let {
                            if (it is PasswordLockError.ConfirmPasswordError) Text(stringResource(it.error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.text_password_warning_note))
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()

                        viewModel.setNewPassword(passwordValue, confirmPasswordValue) {
                            viewModel.setUnlocked()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_confirm))
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.btn_confirm))
                }
            }
        }
    }
}
