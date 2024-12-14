package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun <T> ListDialog(
    text: String,
    list: List<T>,
    initialItem: T? = null,
    onExit: () -> Unit,
    extractDisplayData: (T) -> Pair<String, String>,
    setting: @Composable (Pair<String, String>) -> Unit
) {
    Dialog(
        onDismissRequest = { onExit() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(12.dp, 0.dp, 12.dp, 0.dp)
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                fontSize = 20.sp,
            )
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .heightIn(max = 600.dp)
            ) {
                initialItem?.let { initial ->
                    item {
                        val displayData = extractDisplayData(initial)
                        setting(displayData)
                    }
                }
                items(list) { content ->
                    val displayData = extractDisplayData(content)
                    setting(displayData)
                }
            }
        }
    }
}

@Composable
fun CustomListDialog(
    text: String,
    onExit: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Dialog(
        onDismissRequest = { onExit() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(12.dp, 0.dp, 12.dp, 0.dp)
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                fontSize = 20.sp,
            )
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .heightIn(max = 600.dp)
            ) {
                content()
            }
        }
    }
}