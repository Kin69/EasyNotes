package com.kin.easynotes.presentation.screens.settings.widgets

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ActionType {
    SWITCH,
    LINK,
    TEXT,
    CUSTOM,
    CLIPBOARD
}

@Composable
fun SettingsBox(
    title: String,
    description: String = "",
    icon: ImageVector? = null,
    radius: RoundedCornerShape,
    isEnabled: Boolean = true,
    isCentered: Boolean = false,
    actionType: ActionType,
    variable: Boolean? = null,
    switchEnabled: (Boolean) -> Unit = {},
    linkClicked: () -> Unit = {},
    customAction: @Composable (() -> Unit) -> Unit = {},
    customText: String = "",
    clipboardText: String = ""
) {
    val context = LocalContext.current
    var showCustomAction by remember { mutableStateOf(false) }
    if (showCustomAction) customAction { showCustomAction = !showCustomAction }

    AnimatedVisibility(visible = isEnabled) {
        ElevatedCard(
            shape = radius,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(bottom = 3.dp)
                .clip(radius)
                .clickable {
                    handleAction(
                        context,
                        actionType,
                        variable,
                        switchEnabled,
                        { showCustomAction = !showCustomAction },
                        linkClicked,
                        clipboardText ?: ""
                    )
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = if (description.isNotBlank() || actionType == ActionType.CLIPBOARD) 12.dp else 6.dp)
            ) {
                if (icon != null) RenderIcon(icon)
                if (isCentered) Spacer(Modifier.weight(1f))
                Column {
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 3.dp),
                        fontSize = 16.sp
                    )
                    if (description.isNotEmpty() || actionType == ActionType.CLIPBOARD) {
                        Text(
                            text = if (actionType == ActionType.CLIPBOARD) clipboardText else description,
                            modifier = Modifier.padding(start = 3.dp),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                RenderActionComponent(actionType, variable, switchEnabled, linkClicked, customText, clipboardText)
            }
        }
    }
}

private fun handleAction(
    context: Context,
    actionType: ActionType,
    variable: Boolean?,
    switchEnabled: (Boolean) -> Unit,
    customAction: () -> Unit,
    linkClicked: () -> Unit,
    clipboardText: String
) {
    when (actionType) {
        ActionType.SWITCH -> switchEnabled(!variable!!)
        ActionType.LINK -> linkClicked()
        ActionType.CUSTOM -> customAction()
        ActionType.CLIPBOARD -> copyToClipboard(context, clipboardText)
        ActionType.TEXT -> { /* No action needed */ }
    }
}



@Composable
private fun RenderClipboardAction() {
    Icon(
        imageVector = Icons.Default.ContentCopy,
        contentDescription = null,
        modifier = Modifier.padding(4.dp)
    )
}

fun copyToClipboard(context: Context, clipboardText: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("Copied Text", clipboardText)
    clipboard.setPrimaryClip(clip)
}


@Composable
private fun RenderIcon(icon: ImageVector) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(end = 6.dp)
    )
}

@Composable
private fun RenderActionComponent(
    actionType: ActionType,
    variable: Boolean?,
    switchEnabled: (Boolean) -> Unit,
    linkClicked: () -> Unit,
    customText: String,
    clipboardText: String
) {
    when (actionType) {
        ActionType.SWITCH -> RenderSwitch(variable, switchEnabled)
        ActionType.LINK -> RenderLink(linkClicked)
        ActionType.CUSTOM -> CustomIcon()
        ActionType.TEXT -> RenderText(customText)
        ActionType.CLIPBOARD -> RenderClipboardAction()
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
private fun CustomIcon() {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
        contentDescription = null,
        modifier = Modifier
            .scale(0.6f)
            .padding(12.dp)
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
    shape: RoundedCornerShape,
    isLast: Boolean = false,
    action: () -> Unit = {},
) {
    ElevatedCard(
        shape = shape,
        modifier = Modifier
            .clip(shape)
            .clickable { action() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(shape)
                .fillMaxSize()
                .padding(24.dp, 14.dp, 14.dp, 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            RenderCategoryText(title, subTitle)
            Spacer(modifier = Modifier.weight(1f))
            RenderCategoryIcon(icon)
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
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.padding(9.dp)
        )
    }
}

