package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallSettingCategory(
    title: String,
    subTitle: String,
    icon: ImageVector,
    shape: RoundedCornerShape = RoundedCornerShape(0),
    action: @Composable (() -> Unit) -> Unit,
) {
    var ShowCustomAction by remember { mutableStateOf(false) }
    if (ShowCustomAction) action() { ShowCustomAction = !ShowCustomAction }


    Box(
        modifier = Modifier
            .clip(shape)
            .clickable { ShowCustomAction = !ShowCustomAction }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(30.dp, 10.dp, 18.dp, 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = subTitle,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.padding(end = 8.dp)
                )
                RenderSmallCategoryIcon(icon)
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun RenderSmallCategoryIcon(icon: ImageVector) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(50)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(16.dp)
        )
    }
}