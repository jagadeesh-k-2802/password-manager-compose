package com.jackappsdev.password_manager.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.constants.colorList
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.presentation.theme.PasswordManagerTheme

@Composable
fun ColoredCircle(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: String,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(parseColor(color))
            .size(size)
    ) {
        content?.invoke(this)
    }
}

@Preview
@Composable
private fun CheckmarkCirclePreview() {
    PasswordManagerTheme {
        Box {
            ColoredCircle(color = colorList.first())
        }
    }
}
