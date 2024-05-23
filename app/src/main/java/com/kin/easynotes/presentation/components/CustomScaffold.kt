package com.kin.easynotes.presentation.components

import androidx.compose.foundation.layout.Box
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
        content = {
            Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                content()
            }
        }
    )
}
