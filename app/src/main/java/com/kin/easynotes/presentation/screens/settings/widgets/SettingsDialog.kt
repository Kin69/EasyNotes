package com.kin.easynotes.presentation.screens.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager

@Composable
fun <T> ListDialog(
    text: String,
    list: List<T>,
    initialItem: T? = null,
    settingsViewModel: SettingsViewModel,
    onExit: () -> Unit,
    extractDisplayData: (T) -> Pair<String, String>,
    setting: @Composable (Boolean, Boolean, Pair<String, String>) -> Unit
) {
    Dialog(
        onDismissRequest = { onExit() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = shapeManager(isBoth = true, radius = settingsViewModel.settings.value.cornerRadius)
                )
                .padding(22.dp, 0.dp, 22.dp, 0.dp)
                .fillMaxSize(.8f)
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
            LazyColumn  {
                initialItem?.let { initial ->
                    item {
                        val displayData = extractDisplayData(initial)
                        setting(true, list.size == 1, displayData)
                    }
                }
                itemsIndexed(list) { index, content ->
                    val isFirstItem = (initialItem == null && index == 0)
                    val isLastItem = index == list.lastIndex
                    val displayData = extractDisplayData(content)
                    setting(isFirstItem, isLastItem, displayData)
                    if (isLastItem) Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}