package com.kin.easynotes.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@Composable
fun NotesButton(
    text: String,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        shape = RoundedCornerShape(24.dp),
        onClick = { onClick() },
        icon = { Icon(Icons.Rounded.Edit, text) },
        text = { Text(text = text) },
    )
}
