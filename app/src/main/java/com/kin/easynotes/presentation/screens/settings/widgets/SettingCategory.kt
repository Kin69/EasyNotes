package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingCategory(
    title: String,
    subTitle: String = "",
    icon: ImageVector,
    shape: RoundedCornerShape,
    isLast: Boolean = false,
    smallSetting: Boolean = false,
    action: () -> Unit = {},
    composableAction: @Composable (() -> Unit) -> Unit = {},
) {
    var showCustomAction by remember { mutableStateOf(false) }
    if (showCustomAction) composableAction { showCustomAction = !showCustomAction }

    ElevatedCard(
        shape = shape,
        modifier = Modifier
            .clip(shape)
            .clickable {
                showCustomAction = showCustomAction.not()
                action()
            },
        colors = if (smallSetting) CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary) else CardDefaults.elevatedCardColors(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(shape)
                .fillMaxSize()
                .padding(
                    24.dp,
                    if (smallSetting) 11.dp else 16.dp,
                    14.dp,
                    if (smallSetting) 11.dp else 16.dp
                ),
        ) {
            if (smallSetting) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RenderCategoryTitle(title = title)
                    Spacer(modifier = Modifier.weight(1f))
                    RenderCategoryDescription(subTitle = subTitle, smallSetting = true)
                    RenderCategoryIcon(icon, true)
                }
            } else {
                Column {
                    RenderCategoryTitle(title = title)
                    RenderCategoryDescription(subTitle = subTitle, smallSetting = false)
                }
                Spacer(modifier = Modifier.weight(1f))
                RenderCategoryIcon(icon, false)
            }
        }
    }
    Spacer(modifier = Modifier.height(if (isLast) 26.dp else 2.dp))
}

@Composable
private fun RenderCategoryTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun RenderCategoryDescription(subTitle: String, smallSetting: Boolean) {
    if (subTitle.isNotBlank()) {
        Text(
            color = if (smallSetting) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.primary,
            text = subTitle,
            fontSize = if (smallSetting) 13.sp else 10.sp,
            modifier = Modifier.padding(if (smallSetting) 7.dp else 0.dp)
        )
    }
}

@Composable
private fun RenderCategoryIcon(icon: ImageVector, reverseColors: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (reverseColors) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (reverseColors) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier
                .scale(if (reverseColors) 0.8f else 1f)
                .padding(if (reverseColors) 9.dp else 9.dp)
        )
    }
}

