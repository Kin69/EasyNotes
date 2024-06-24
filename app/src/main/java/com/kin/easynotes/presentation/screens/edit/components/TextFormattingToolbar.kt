package com.kin.easynotes.presentation.screens.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.HMobiledata
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel

@Composable
fun TextFormattingToolbar(viewModel: EditViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { viewModel.insertText("- ") }) {
            Icon(Icons.AutoMirrored.Rounded.FormatListBulleted, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = { viewModel.insertText("```\n\n```",-4) }) {
            Icon(Icons.Rounded.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = { viewModel.insertText("# ") }) {
            Icon(Icons.Rounded.HMobiledata, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = { viewModel.insertText("> ") }) {
            Icon(Icons.Rounded.FormatQuote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = { viewModel.insertText("[ ] ") }) {
            Icon(Icons.Rounded.CheckBox, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}
