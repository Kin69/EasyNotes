package com.kin.easynotes.presentation.screens.edit.components

import android.content.Context

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.FormatUnderlined
import androidx.compose.material.icons.rounded.HMobiledata
import androidx.compose.material.icons.rounded.Highlight
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.StrikethroughS
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kin.easynotes.presentation.components.getExternalStorageDir
import com.kin.easynotes.presentation.components.getImageName
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

data class ToolbarItem(
    val icon: ImageVector,
    val contentDescription: String,
    val color: Color,
    val onClickAction: () -> Unit,
)

@Composable
fun TextFormattingToolbar(viewModel: EditViewModel) {
    val colorArrow = MaterialTheme.colorScheme.outlineVariant
    val colorIcon = MaterialTheme.colorScheme.inverseSurface
    var currentIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val savedUri = saveImageToAppStorage(context, it)
            viewModel.insertText("!($savedUri)")
        }
    }

    val toolbarSets = remember {
        listOf(
            listOf(
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowBackIos,"Bullet List", color = colorArrow) {

                },
                ToolbarItem(Icons.AutoMirrored.Rounded.FormatListBulleted, "Bullet List", color = colorIcon) {
                    viewModel.insertText("- ")
                },
                ToolbarItem(Icons.Rounded.CheckBox, "Checkbox", color = colorIcon) {
                    viewModel.insertText("[ ] ")
                },
                ToolbarItem(Icons.Rounded.Image, "Insert Image", color = colorIcon) {
                    launcher.launch("image/*")
                },
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowForwardIos,"Bullet List", color = colorArrow) {
                        currentIndex++
                },
            ),
            listOf(
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowBackIos,"Bullet List", color = colorArrow) {
                    currentIndex--
                },
                ToolbarItem(Icons.Rounded.FormatBold, "Bold", color = colorIcon) {
                    viewModel.insertText("****", offset = -2, newLine = false)
                },
                ToolbarItem(Icons.Rounded.HMobiledata, "Header", color = colorIcon) {
                    viewModel.insertText("# ")
                },
                ToolbarItem(Icons.Rounded.FormatItalic, "Italic", color = colorIcon) {
                    viewModel.insertText("**", offset = -1, newLine = false)
                },
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowForwardIos,"Bullet List", color = colorArrow) {
                    currentIndex++
                },
            ),
            listOf(
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowBackIos,"Bullet List", color = colorArrow) {
                    currentIndex--
                },
                ToolbarItem(Icons.Rounded.StrikethroughS, "Strikethrough", color = colorIcon) {
                    viewModel.insertText("~~~~", -2 , newLine = false)
                },
                ToolbarItem(Icons.Rounded.Highlight, "Highlight", color = colorIcon) {
                    viewModel.insertText("====", offset = -2, newLine = false)
                },
                ToolbarItem(Icons.Rounded.Code, "Code Block", color = colorIcon) {
                    viewModel.insertText("```\n\n```", -4)
                },
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowForwardIos,"Bullet List", color = colorArrow) {
                    currentIndex++
                },
            ),
            listOf(
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowBackIos,"Bullet List", color = colorArrow) {
                    currentIndex--
                },
                ToolbarItem(Icons.Rounded.FormatUnderlined, "Underline", color = colorIcon) {
                    viewModel.insertText("__", -1 , newLine = false)
                },
                ToolbarItem(Icons.Rounded.FormatQuote, "Quote", color = colorIcon) {
                    viewModel.insertText("> ", newLine = true)
                },
                ToolbarItem(Icons.AutoMirrored.Rounded.ArrowForwardIos,"Bullet List", color = colorArrow) {

                },
            ),
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        toolbarSets.getOrNull(currentIndex)?.let { toolbarItems ->
            toolbarItems.forEach { item ->
                IconButton(
                    onClick = { item.onClickAction.invoke() },
                ) {
                    Icon(
                        item.icon,
                        contentDescription = item.contentDescription,
                        modifier = Modifier.size(20.dp),
                        tint = item.color,
                    )
                }
            }
        }
    }
}

private fun saveImageToAppStorage(context: Context, uri: Uri): String {
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
    return imageFile.path.toString()
}