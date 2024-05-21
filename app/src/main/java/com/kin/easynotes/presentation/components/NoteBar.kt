package com.kin.easynotes.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kin.easynotes.presentation.theme.GlobalFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    titleText: String = "",
    onBackNavClicked: (() -> Unit)? = null,
    onSettingsClicked:  () -> Unit = {},
    onDeleteClicked:  (() -> Unit)? = null,
    onSaveClicked:  (() -> Unit)? = null,
) {
    TopAppBar(
        title = { TitleText(titleText) },
        navigationIcon = { NavigationIcon(titleText, onBackNavClicked  ?: {})},
        actions = {
            Row {
                if (onSaveClicked != null) SaveButton(onSaveClicked)
                if (onDeleteClicked != null) DeleteButton(onDeleteClicked)
                SettingsButton(titleText, onSettingsClicked)
            }
        },
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TitleText(titleText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titleText,
            fontFamily = GlobalFont,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NavigationIcon(titleText: String, onBackNavClicked: () -> Unit) {
    if (!titleText.equals("notes", ignoreCase = true)) {
        IconButton(onClick = onBackNavClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun SaveButton(onSaveClicked: () -> Unit) {
    IconButton(onClick = onSaveClicked) {
        Icon(
            imageVector = Icons.Rounded.Done,
            contentDescription = "Done",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun DeleteButton(onDeleteClicked:  () -> Unit) {
    IconButton(onClick = onDeleteClicked) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SettingsButton(titleText: String, onSettingsClicked: () -> Unit) {
    if (titleText.equals("notes", ignoreCase = true)) {
        IconButton(
            onClick = onSettingsClicked
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}