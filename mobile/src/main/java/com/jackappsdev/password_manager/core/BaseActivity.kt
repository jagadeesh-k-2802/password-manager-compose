package com.jackappsdev.password_manager.core

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.PlatformTextInputMethodRequest
import androidx.compose.ui.platform.PlatformTextInputSession
import androidx.fragment.app.FragmentActivity
import com.jackappsdev.password_manager.BuildConfig
import com.jackappsdev.password_manager.presentation.main.MainViewModel
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base Activity to handle common functionality across all activities
 */
@AndroidEntryPoint
@OptIn(ExperimentalComposeUiApi::class)
abstract class BaseActivity : FragmentActivity() {

    protected val passwordLockViewModel: PasswordLockViewModel by viewModels()
    protected val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Disable autofill for the entire app
        window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS

        // Disable screenshots & screen recordings for production builds
        if (BuildConfig.DEBUG.not()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    protected suspend fun handlePlatformTextInput(
        request: PlatformTextInputMethodRequest,
        nextHandler: PlatformTextInputSession,
        useIncognitoKeyboard: Boolean
    ): Nothing {
        if (useIncognitoKeyboard) {
            val modifiedRequest = PlatformTextInputMethodRequest { outAttributes ->
                request.createInputConnection(outAttributes).also {
                    NoPersonalizedLearningHelper.addNoPersonalizedLearning(outAttributes)
                }
            }
            nextHandler.startInputMethod(modifiedRequest)
        } else {
            nextHandler.startInputMethod(request)
        }
    }
}
