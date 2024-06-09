package com.kin.easynotes.presentation.screens.edit.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.HMobiledata
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel

@Composable
fun TextFormattingToolbar(viewModel: EditViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { viewModel.insertText("- ") }) {
            Icon(Icons.AutoMirrored.Rounded.FormatListBulleted, contentDescription = "bullet")
        }
        IconButton(onClick = { viewModel.insertText("```\n\n```",-4) }) {
            Icon(Icons.Rounded.Code, contentDescription = "Code Block")
        }
        IconButton(onClick = { viewModel.insertText("# ") }) {
            Icon(Icons.Rounded.HMobiledata, contentDescription = "Size")
        }
        IconButton(onClick = { viewModel.insertText("> ") }) {
            Icon(Icons.Rounded.FormatQuote, contentDescription = "Quote")
        }
        IconButton(onClick = { viewModel.insertText("[ ] ") }) {
            Icon(Icons.Rounded.CheckBox, contentDescription = "CheckBox")
        }
    }
}
