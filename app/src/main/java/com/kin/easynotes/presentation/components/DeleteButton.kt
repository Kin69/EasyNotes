package com.kin.easynotes.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DeleteButton(onDeleteClicked:  () -> Unit) {
    IconButton(onClick = onDeleteClicked) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
