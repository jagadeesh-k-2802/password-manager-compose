package com.jackappsdev.password_manager.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

// TODO: REPLACE ALL EMPTY VIEWS WITH THIS ONE
@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.task_empty_state,
    @StringRes title: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(stringResource(title))
    }
}

@Preview
@Composable
private fun EmptyStateViewPreview() {
    PasswordManagerTheme {
        EmptyStateView(
            icon = R.drawable.task_empty_state,
            title = R.string.text_no_passwords
        )
    }
}
