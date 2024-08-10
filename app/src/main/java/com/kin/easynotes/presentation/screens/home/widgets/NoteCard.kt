package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.markdown.MarkdownText
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    settingsViewModel: SettingsViewModel,
    containerColor: Color,
    note: Note,
    isBorderEnabled: Boolean,
    shape: RoundedCornerShape,
    onShortClick: () -> Unit,
    onLongClick: () -> Unit,
    onNoteUpdate: (Note) -> Unit
) {
    val borderModifier = if (isBorderEnabled) {
        Modifier.border(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = shape
        )
    } else {
        if (containerColor != Color.Black) Modifier else
            Modifier.border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = shape
            )
    }

    ElevatedCard(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .clip(shape)
            .combinedClickable(
                onClick = { onShortClick() },
                onLongClick = { onLongClick() }
            )
            .then(borderModifier),
        elevation = CardDefaults.cardElevation(defaultElevation = if (containerColor != Color.Black) 6.dp else 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 12.dp)
        ) {
            if (note.name.isNotBlank()) {
                MarkdownText(
                    isPreview = true,
                    isEnabled = settingsViewModel.settings.value.isMarkdownEnabled,
                    markdown = note.name.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_name_height))
                        .then(
                            if (note.description.isNotBlank() && !settingsViewModel.settings.value.showOnlyTitle) {
                                Modifier.padding(bottom = 9.dp)
                            } else {
                                Modifier
                            }
                        ),
                    weight = FontWeight.Bold,
                    spacing = 0.dp,
                    onContentChange = { onNoteUpdate(note.copy(name = it)) },
                    fontSize = 16.sp,
                    radius = settingsViewModel.settings.value.cornerRadius
                )
            }
            if (note.description.isNotBlank() && !settingsViewModel.settings.value.showOnlyTitle) {
                MarkdownText(
                    isPreview = true,
                    markdown = note.description,
                    isEnabled = settingsViewModel.settings.value.isMarkdownEnabled,
                    spacing = 0.dp,
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_description_height)),
                    onContentChange = { onNoteUpdate(note.copy(description = it)) },
                    fontSize = 14.sp,
                    radius = settingsViewModel.settings.value.cornerRadius
                )
            }
        }
    }
}
