package com.kin.easynotes.presentation.screens.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material.icons.rounded.FormatListNumbered
import androidx.compose.material.icons.rounded.Image
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
        IconButton(onClick = { viewModel.toggleBold() }) {
            Icon(Icons.Rounded.FormatBold, contentDescription = "Bold", Modifier.fillMaxSize(0.5F))
        }
        IconButton(onClick = { viewModel.toggleItalic() }) {
            Icon(Icons.Rounded.FormatItalic, contentDescription = "Italic")
        }
        IconButton(onClick = { viewModel.toggleItalic() }) {
            Icon(Icons.Rounded.Image, contentDescription = "Image")
        }
        IconButton(onClick = { viewModel.toggleItalic() }) {
            Icon(Icons.Rounded.FormatListNumbered, contentDescription = "Image")
        }
    }
}
