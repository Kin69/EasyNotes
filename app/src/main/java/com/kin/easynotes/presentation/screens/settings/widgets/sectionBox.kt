package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kin.easynotes.presentation.theme.GlobalFont

@Composable
fun SettingSection(
    sectionName: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {
        Text(
            sectionName,
            fontFamily = GlobalFont,
            fontSize = 16.sp
        )
    }
    content()
}