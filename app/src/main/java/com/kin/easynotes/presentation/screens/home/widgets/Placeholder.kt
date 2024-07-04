package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Placeholder(
    placeholderIcon: @Composable () -> Unit,
    placeholderText: String
) {
    Box(
        modifier = Modifier
            .imePadding()
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            placeholderIcon()

            Text(
                text = placeholderText,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp
            )
        }
    }
}