package com.jackappsdev.password_manager.autofill

import android.os.Bundle
import android.view.WindowManager
import android.view.autofill.AutofillId
import android.view.autofill.AutofillManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import com.jackappsdev.password_manager.BuildConfig
import com.jackappsdev.password_manager.autofill.model.FillResult
import com.jackappsdev.password_manager.core.parcelable
import com.jackappsdev.password_manager.presentation.main.MainViewModel
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AutofillActivity : FragmentActivity() {

    private val passwordLockViewModel: PasswordLockViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val usernameId: AutofillId? by lazy { intent?.parcelable(EXTRA_USERNAME_ID) }
    private val passwordId: AutofillId? by lazy { intent?.parcelable(EXTRA_PASSWORD_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Disable screenshots & screen recordings for production builds
        if (BuildConfig.DEBUG.not()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        setContent {
            val isUnlocked by passwordLockViewModel.hasBeenUnlockedFlow.collectAsState(false)

            PasswordManagerTheme(dynamicColor = mainViewModel.useDynamicColors == true) {
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
