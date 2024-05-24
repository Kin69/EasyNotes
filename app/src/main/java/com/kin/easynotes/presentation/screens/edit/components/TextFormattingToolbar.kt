package com.kin.easynotes.presentation.screens.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel

@Composable
fun TextFormattingToolbar(viewModel: EditViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { viewModel.updateNoteNameDescription(viewModel.noteDescriptionState.value + "\n- " ) }) {
            Icon(Icons.AutoMirrored.Rounded.FormatListBulleted, contentDescription = "bullet")
        }
        IconButton(onClick = { viewModel.updateNoteNameDescription(viewModel.noteDescriptionState.value + "\n```\n```") }) {
            Icon(Icons.Rounded.FormatQuote, contentDescription = "Quote")
        }
        IconButton(onClick = { viewModel.updateNoteNameDescription(viewModel.noteDescriptionState.value + "\n#") }) {
            Icon(Icons.Rounded.FormatSize, contentDescription = "Size")
        }
    }
}
