package com.jackappsdev.password_manager.autofill

import android.os.Bundle
import android.view.autofill.AutofillId
import android.view.autofill.AutofillManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import com.jackappsdev.password_manager.autofill.model.FillResult
import com.jackappsdev.password_manager.core.BaseActivity
import com.jackappsdev.password_manager.core.parcelable
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockRoot
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@OptIn(ExperimentalComposeUiApi::class)
class AutofillActivity : BaseActivity() {

    private val usernameId: AutofillId? by lazy { intent?.parcelable(EXTRA_USERNAME_ID) }
    private val passwordId: AutofillId? by lazy { intent?.parcelable(EXTRA_PASSWORD_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isUnlocked by passwordLockViewModel.hasBeenUnlockedFlow.collectAsState(false)

            PasswordManagerTheme(dynamicColor = mainViewModel.useDynamicColors == true) {
                InterceptPlatformTextInput(
                    interceptor = { request, nextHandler ->
                        handlePlatformTextInput(
                            request,
                            nextHandler,
                            mainViewModel.useIncognitoKeyboard == true
                        )
                    }
                ) {
                    when (isUnlocked) {
                        true -> {
                            AutofillRoot(
                                usernameId = usernameId,
                                passwordId = passwordId,
                                onResult = ::onResult
                            )
                        }

                        false -> {
                            PasswordLockRoot(
                                passwordLockViewModel = passwordLockViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    fun onResult(result: FillResult) {
        when (result) {
            is FillResult.Success -> {
                val data = android.content.Intent().apply {
                    putExtra(
                        AutofillManager.EXTRA_AUTHENTICATION_RESULT,
                        result.response
                    )
                }
                setResult(RESULT_OK, data)
                finish()
            }

            FillResult.Cancelled -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME_ID = "extra_username_id"
        const val EXTRA_PASSWORD_ID = "extra_password_id"
        const val EXTRA_CLIENT_STATE = "extra_client_state"
    }
}
