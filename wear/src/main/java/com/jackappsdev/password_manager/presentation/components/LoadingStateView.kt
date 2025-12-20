package com.jackappsdev.password_manager.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.CircularProgressIndicator
import com.jackappsdev.password_manager.presentation.screens.base.WearPreview
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun LoadingStateView(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@WearPreview
@Composable
private fun LoadingStateViewPreview() {
    PasswordManagerTheme {
        LoadingStateView()
    }
}
