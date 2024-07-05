package com.kin.easynotes.presentation.screens.edit.components

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.FileObserver
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun TextFormattingToolbar(viewModel: EditViewModel) {
    val activity  = LocalContext.current as Activity
    val context = LocalContext.current


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
        ImagePicker { photoUri ->
            viewModel.insertText("!($photoUri)")
        }
    }
}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri
        if (uri != null) {
            val savedUri = saveImageToMediaStore(context, uri)
            onImageSelected(savedUri)
        }
    }
    IconButton(onClick = { launcher.launch("image/*") }) {
        Icon(Icons.Rounded.Image, contentDescription = "Select Image")
    }
}

private fun saveImageToMediaStore(context: Context, uri: Uri): Uri {
    val contentResolver: ContentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    }

    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: throw IllegalStateException("Failed to create MediaStore entry")

    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    inputStream?.use { input ->
        contentResolver.openOutputStream(imageUri)?.use { output ->
            input.copyTo(output)
        }
    }

    inputStream?.close()

    return imageUri
}

private fun getExternalStorageDir(context: Context): File {
    return context.getExternalFilesDir(null) ?: throw IllegalStateException("External storage directory not found")
}