package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleWrapper(
    color: Color = MaterialTheme.colorScheme.background,
    size: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = color,
                shape = RoundedCornerShape(50)
            )
            .padding(size),
    ) {
        content()
    }
}