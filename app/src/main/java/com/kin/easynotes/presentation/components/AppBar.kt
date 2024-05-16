package com.kin.easynotes.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    titleText: String = "",
    onBackNavClicked: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    onDeleteEnabled: Boolean = false,
    onSaveEnabled: Boolean = false,
    onSaveClicked: () -> Unit = {}
) {
    TopAppBar(
        title = { TitleText(titleText) },
        navigationIcon = { NavigationIcon(titleText, onBackNavClicked) },
        actions = { ActionButtons(onSaveEnabled, onSaveClicked, onDeleteEnabled, onDeleteClicked, onSettingsClicked, titleText) },
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
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NavigationIcon(titleText: String, onBackNavClicked: () -> Unit) {
    if (!titleText.equals("notes", ignoreCase = true)) {
        IconButton(onClick = onBackNavClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ActionButtons(
    onSaveEnabled: Boolean,
    onSaveClicked: () -> Unit,
    onDeleteEnabled: Boolean,
    onDeleteClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    titleText: String
) {
    Row {
        SaveButton(onSaveEnabled, onSaveClicked)
        DeleteButton(onDeleteEnabled, onDeleteClicked)
        SettingsButton(titleText, onSettingsClicked)
    }
}

@Composable
private fun SaveButton(onSaveEnabled: Boolean, onSaveClicked: () -> Unit) {
    if (onSaveEnabled) {
        IconButton(onClick = onSaveClicked) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Done",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun DeleteButton(onDeleteEnabled: Boolean, onDeleteClicked: () -> Unit) {
    if (onDeleteEnabled) {
        IconButton(onClick = onDeleteClicked) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun SettingsButton(titleText: String, onSettingsClicked: () -> Unit) {
    if (titleText.equals("notes", ignoreCase = true)) {
        IconButton(
            onClick = onSettingsClicked
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}