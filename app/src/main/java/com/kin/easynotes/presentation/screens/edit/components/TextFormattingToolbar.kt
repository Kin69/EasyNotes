package com.kin.easynotes.presentation.screens.edit.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kin.easynotes.presentation.components.getExternalStorageDir
import com.kin.easynotes.presentation.components.getImageName
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun TextFormattingToolbar(viewModel: EditViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { viewModel.insertText("- ") }) {
            Icon(Icons.AutoMirrored.Rounded.FormatListBulleted, contentDescription = null)
        }
        IconButton(onClick = { viewModel.insertText("```\n\n```",-4) }) {
            Icon(Icons.Rounded.Code, contentDescription = null)
        }
        IconButton(onClick = { viewModel.insertText("# ") }) {
            Icon(Icons.Rounded.HMobiledata, contentDescription = null)
        }
        IconButton(onClick = { viewModel.insertText("> ") }) {
            Icon(Icons.Rounded.FormatQuote, contentDescription = null)
        }
        IconButton(onClick = { viewModel.insertText("[ ] ") }) {
            Icon(Icons.Rounded.CheckBox, contentDescription = null)
        }
        ImagePicker(viewModel) { photoUri ->
            viewModel.insertText("!($photoUri)")
            viewModel.toggleIsInsertingImages(false)
        }
    }
}

@Composable
fun ImagePicker(viewModel: EditViewModel, onImageSelected: (Uri) -> Unit) {
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri
        if (uri != null) {
            val savedUri = saveImageToAppStorage(context, uri)
            onImageSelected(savedUri)
        }
    }
    IconButton(onClick = {
        viewModel.toggleIsInsertingImages(true)
        launcher.launch("image/*")
    }) {
        Icon(Icons.Rounded.Image, contentDescription = "Select Image")
    }
}

private fun saveImageToAppStorage(context: Context, uri: Uri): Uri {
    val appStorageDir = getExternalStorageDir(context)
    if (!appStorageDir.exists()) {
        appStorageDir.mkdirs()
    }
    val imageFile = File(appStorageDir, getImageName(uri))

    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    inputStream?.use { input ->
        FileOutputStream(imageFile).use { output ->
            input.copyTo(output)
        }
    }

    inputStream?.close()

    return Uri.fromFile(imageFile)
}