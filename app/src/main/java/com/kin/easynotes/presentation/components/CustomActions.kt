package com.kin.easynotes.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CloseButton(
    contentDescription: String = "Close",
    onCloseClicked:  () -> Unit
) {
    IconButton(onClick = onCloseClicked) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun MoreButton(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(Icons.Rounded.MoreVert, contentDescription = "Info")
    }
}

@Composable
fun SaveButton(onSaveClicked: () -> Unit) {
    IconButton(onClick = onSaveClicked) {
        Icon(
            imageVector = Icons.Rounded.Done,
            contentDescription = "Done",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun UndoButton(onUndoClicked: () -> Unit) {
    IconButton(onClick = onUndoClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Undo,
            contentDescription = "Undo",
            tint = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun RedoButton(onRedoClicked: () -> Unit) {
    IconButton(onClick = onRedoClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Redo,
            contentDescription = "Redo",
            tint = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun NavigationIcon(onBackNavClicked: () -> Unit) {
    IconButton(onClick = onBackNavClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SettingsButton(onSettingsClicked: () -> Unit) {
    IconButton(onClick = onSettingsClicked) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun VaultButton(vaultEnabled: Boolean,onVaultButtonClicked: () -> Unit) {
    IconButton(onClick = onVaultButtonClicked) {
        Icon(
            imageVector = if (vaultEnabled) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
            contentDescription = "Vault",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TitleText(titleText: String) {
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
fun PinButton(isPinned: Boolean, onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(if (isPinned) Icons.Rounded.PushPin else Icons.Outlined.PushPin, contentDescription = "Pin")
    }
}

@Composable
fun DeleteButton(onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            imageVector =  Icons.Rounded.Delete,
            contentDescription = "Delete",
        )
    }
}

@Composable
fun SelectAllButton(enabled: Boolean, onClick: () -> Unit) {
    if (enabled) {
        IconButton(
            onClick = { onClick() }
        ) {
            Icon(
                imageVector =  Icons.Rounded.SelectAll,
                contentDescription = "Select All",
            )
        }
    }
}