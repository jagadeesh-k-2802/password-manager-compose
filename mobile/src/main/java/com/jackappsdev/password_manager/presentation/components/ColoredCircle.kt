package com.jackappsdev.password_manager.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.core.parseColor

// TODO: REPLACE ALL CATEGORY COLOR CIRCLE WITH THIS
@Composable
fun ColoredCircle(
    modifier: Modifier = Modifier,
    color: String
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(parseColor(color))
            .size(24.dp)
    ) {}
}
