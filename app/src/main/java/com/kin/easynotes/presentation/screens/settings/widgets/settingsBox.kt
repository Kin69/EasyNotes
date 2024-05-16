package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SettingsBox(
    title: String,
    variable: Boolean? = null,
    radius: Array<Dp> = arrayOf(0.dp, 0.dp,0.dp,0.dp),
    customAction: @Composable (() -> Unit)? = null,
    onClicked: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(radius[0],radius[1],radius[2],radius[3]))
            .clickable { onClicked() }
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(radius[0],radius[1],radius[2],radius[3])
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(title, modifier = Modifier.padding(start = 3.dp))
        Spacer(modifier = Modifier.weight(1f))
        if (customAction == null && variable != null) {
            Switch(
                checked = variable,
                onCheckedChange = { onClicked() },
                modifier = Modifier
                    .scale(0.9f)
                    .padding(0.dp)
            )
        } else if (customAction != null) {
            customAction()
            Spacer(modifier = Modifier.padding(vertical = 24.dp))
        }
    }
}