package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MaterialText(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    titleSize: TextUnit = 14.sp,
    descriptionSize: TextUnit = 11.sp,
    center: Boolean = false,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    descriptionColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start,) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = titleSize),
            color = titleColor,
            modifier = Modifier.padding(bottom = 3.dp),
            textAlign = if (center) TextAlign.Center else TextAlign.Start
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = descriptionSize),
                color = descriptionColor,
                modifier = Modifier.padding(bottom = 3.dp),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}