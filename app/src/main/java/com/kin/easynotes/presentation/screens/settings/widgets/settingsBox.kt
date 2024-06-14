package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ActionType {
    SWITCH,
    LINK,
    TEXT
}

@Composable
fun SettingsBox(
    title: String,
    icon: ImageVector,
    actionType: ActionType,
    variable: Boolean? = null,
    shape: RoundedCornerShape = RoundedCornerShape(0),
    switchEnabled: (Boolean) -> Unit = {},
    linkClicked: () -> Unit = {},
    customText: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape)
            .clickable {
                when (actionType) {
                    ActionType.SWITCH -> switchEnabled(!variable!!)
                    ActionType.LINK -> linkClicked()
                    ActionType.TEXT -> { /* No action needed */ }
                }
            }
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 6.dp)
        )
        Text(
            text = title,
            modifier = Modifier.padding(start = 3.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        when (actionType) {
            ActionType.SWITCH -> {
                Switch(
                    checked = variable ?: false,
                    onCheckedChange = { switchEnabled(it) },
                    modifier = Modifier.scale(0.9f).padding(0.dp)
                )
            }
            ActionType.LINK -> {
                Icon(
                    imageVector = Icons.Rounded.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { linkClicked() }
                )
            }
            ActionType.TEXT -> {
                Text(
                    text = customText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}
