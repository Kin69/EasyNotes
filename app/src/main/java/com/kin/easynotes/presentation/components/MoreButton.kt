package com.kin.easynotes.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun MoreButton(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(Icons.Rounded.MoreVert, contentDescription = "Info")
    }
}