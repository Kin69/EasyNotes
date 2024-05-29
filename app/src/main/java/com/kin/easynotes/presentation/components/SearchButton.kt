package com.kin.easynotes.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SearchButton(onSearchClicked: () -> Unit) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = "search",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}