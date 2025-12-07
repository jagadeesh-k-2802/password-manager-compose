package com.jackappsdev.password_manager.autofill

import android.view.autofill.AutofillId
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jackappsdev.password_manager.autofill.model.FillResult

@Composable
fun AutofillRoot(
    usernameId: AutofillId?,
    passwordId: AutofillId?,
    onResult: (FillResult) -> Unit
) {
    val viewModel: AutofillViewModel = hiltViewModel()
    val context = LocalContext.current

    val effectHandler = remember {
        AutofillEffectHandler(
            context = context,
            usernameId = usernameId,
            passwordId = passwordId,
            onResult = onResult
        )
    }

    AutofillScreen(
        state = viewModel.state,
        effectFlow = viewModel.effectFlow,
        effectHandler = effectHandler,
        onEvent = viewModel::onEvent
    )
}
