package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier

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
    switchEnabled: (Boolean) -> Unit = {},
    linkClicked: () -> Unit = {},
    customText: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { handleAction(actionType, variable, switchEnabled, linkClicked) }
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        RenderIcon(icon)
        Text(
            text = title,
            modifier = Modifier.padding(start = 3.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        RenderActionComponent(actionType, variable, switchEnabled, linkClicked, customText)
    }
    Spacer(modifier = Modifier.height(2.dp))
}

private fun handleAction(
    actionType: ActionType,
    variable: Boolean?,
    switchEnabled: (Boolean) -> Unit,
    linkClicked: () -> Unit
) {
    when (actionType) {
        ActionType.SWITCH -> switchEnabled(!variable!!)
        ActionType.LINK -> linkClicked()
        ActionType.TEXT -> { /* No action needed */ }
    }
}

@Composable
private fun RenderIcon(icon: ImageVector) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(end = 6.dp)
    )
}

@Composable
private fun RenderActionComponent(
    actionType: ActionType,
    variable: Boolean?,
    switchEnabled: (Boolean) -> Unit,
    linkClicked: () -> Unit,
    customText: String
) {
    when (actionType) {
        ActionType.SWITCH -> RenderSwitch(variable, switchEnabled)
        ActionType.LINK -> RenderLink(linkClicked)
        ActionType.TEXT -> RenderText(customText)
    }
}

@Composable
private fun RenderSwitch(variable: Boolean?, switchEnabled: (Boolean) -> Unit) {
    Switch(
        checked = variable ?: false,
        onCheckedChange = { switchEnabled(it) },
        modifier = Modifier
            .scale(0.9f)
            .padding(0.dp)
    )
}

@Composable
private fun RenderLink(linkClicked: () -> Unit) {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
        contentDescription = null,
        modifier = Modifier
            .padding(12.dp)
            .clickable { linkClicked() }
    )
}

@Composable
private fun RenderText(customText: String) {
    Text(
        text = customText,
        fontSize = 14.sp,
        modifier = Modifier.padding(12.dp)
    )
}

@Composable
fun SettingCategory(
    title: String,
    subTitle: String,
    icon: ImageVector,
    shape: RoundedCornerShape = RoundedCornerShape(0),
    isLast: Boolean = false,
    action: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .clickable { action() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 30.dp, vertical = 14.dp)
                .fillMaxWidth()
        ) {

            Row {
                RenderCategoryText(title, subTitle)
                Spacer(modifier = Modifier.weight(1f))
                RenderCategoryIcon(icon)
            }
        }
    }
    Spacer(modifier = Modifier.height(if (isLast) 26.dp else 2.dp))
}

@Composable
private fun RenderCategoryText(title: String, subTitle: String) {
    Column(
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subTitle,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun RenderCategoryIcon(icon: ImageVector) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    }
}

@Composable
fun SmallSettingCategory(
    title: String,
    subTitle: String,
    icon: ImageVector,
    shape: RoundedCornerShape = RoundedCornerShape(0),
    action: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .clickable { action() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 30.dp, vertical = 10.dp)
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
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.padding(end = 6.dp)
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
            modifier = Modifier.height(22.dp)
        )
    }
}
