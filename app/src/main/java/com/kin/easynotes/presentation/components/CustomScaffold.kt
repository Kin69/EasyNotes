package com.kin.easynotes.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotesScaffold(
    topBar : @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = { topBar() },
        floatingActionButton = { floatingActionButton() },
        content = { padding ->
            // NOTE: imePadding seems to apply the navigationBarsPadding again instead of consuming it
            // so this workaround is needed until this bug: https://issuetracker.google.com/issues/249727298
            // is fixed on google's end.
            Box(modifier = Modifier.padding(padding).consumeWindowInsets(padding)) {
                content()
            }
        }
    )
}
