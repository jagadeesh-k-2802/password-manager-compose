package com.jackappsdev.password_manager.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.core.getPasswordStrengthColor
import com.jackappsdev.password_manager.core.getPasswordStrengthColorDark
import com.jackappsdev.password_manager.core.getPasswordStrengthText
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun PasswordStrengthIndicator(
    password: String,
    modifier: Modifier = Modifier
) {
    if (password.isEmpty()) return

    val length = password.length
    val strengthText = stringResource(getPasswordStrengthText(length))
    val strengthColor = if (isSystemInDarkTheme()) {
        getPasswordStrengthColorDark(length)
    } else {
        getPasswordStrengthColor(length)
    }

    val barsToFill = when {
        length <= 5 -> 1
        length <= 8 -> 2
        length <= 12 -> 3
        else -> 4
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..4) {
                val isFilled = i <= barsToFill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isFilled) strengthColor else Color.Gray.copy(alpha = 0.2f))
                )
            }
        }
        Text(
            text = strengthText,
            style = MaterialTheme.typography.labelSmall,
            color = strengthColor
        )
    }
}

@Preview
@Composable
fun PasswordStrengthIndicatorPreview() {
    PasswordManagerTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            PasswordStrengthIndicator(password = "123")
            PasswordStrengthIndicator(password = "123456")
            PasswordStrengthIndicator(password = "123456789012")
            PasswordStrengthIndicator(password = "123456789012345")
        }
    }
}
