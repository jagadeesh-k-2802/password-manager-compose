package com.jackappsdev.password_manager.presentation.screens.password_lock

import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.navigation.replace
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordLockScreen(
    navController: NavController,
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

    LaunchedEffect(state.hasPasswordSet, state.useScreenLockToUnlock) {
        if (state.hasPasswordSet == true && state.useScreenLockToUnlock == true) {
            val executor = context.mainExecutor

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Password Manager")
                .setSubtitle("Continue by using the same unlocking method as you would on your lock screen.")
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                .build()

            val biometricPrompt = BiometricPrompt(
                context,
                executor,
                object :
                    BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        navController.replace(Routes.Home)
                    }
                }
            )

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
                CenterAlignedTopAppBar(title = { Text("Enter Your Password") })
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { value -> passwordValue = value },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error is PasswordLockError.PasswordError,
                    supportingText = {
                        error?.let {
                            if (it is PasswordLockError.PasswordError) Text(it.error)
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
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
                                navController.replace(Routes.Home)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Done, "Confirm")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Confirm")
                }
            } else {
                CenterAlignedTopAppBar(title = { Text("Create Your Password") })
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { value -> passwordValue = value },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error is PasswordLockError.PasswordError,
                    supportingText = {
                        error?.let {
                            if (it is PasswordLockError.PasswordError) Text(it.error)
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )

                OutlinedTextField(
                    value = confirmPasswordValue,
                    onValueChange = { value -> confirmPasswordValue = value },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error is PasswordLockError.ConfirmPasswordError,
                    supportingText = {
                        error?.let {
                            if (it is PasswordLockError.ConfirmPasswordError) Text(it.error)
                        }
                    },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "WARNING: Do not forgot this password else you will lose " +
                            "access to all of your saved passwords."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()

                        viewModel.setNewPassword(passwordValue, confirmPasswordValue) {
                            navController.replace(Routes.Home)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Done, "Confirm")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Confirm")
                }
            }
        }
    }
}
