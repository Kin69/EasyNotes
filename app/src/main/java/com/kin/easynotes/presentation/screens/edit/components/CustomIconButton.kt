package com.kin.easynotes.presentation.screens.edit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    shape: RoundedCornerShape,
    elevation: Dp,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        modifier = Modifier
            .clip(shape)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier.padding(16.dp),
        )
    }
}