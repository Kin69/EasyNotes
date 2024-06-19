package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    containerColor : Color,
    borderColor : Color,
    shape: RoundedCornerShape,
    onShortClick : () -> Unit,
    onLongClick : () -> Unit,
    onNoteUpdate: (Note) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .clip(shape)
            .background(containerColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = shape
            )
            .combinedClickable(
                onClick = { onShortClick() },
                onLongClick = { onLongClick() }
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp,12.dp,16.dp,12.dp)
        ) {
            if (note.name.isNotBlank()) {
                MarkdownText(
                    markdown = note.name.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_name_height))
                        .padding(bottom = 4.dp),
                    weight = FontWeight.Bold,
                    spacing = 0.dp,
                    onContentChange = { onNoteUpdate(note.copy(name = it)) },
                    fontSize = 16.sp
                )
            }
            if (note.description.isNotBlank()) {
                MarkdownText(
                    markdown = note.description,
                    spacing = 0.dp,
                    modifier = Modifier.heightIn(max = dimensionResource(R.dimen.max_description_height)),
                    onContentChange = { onNoteUpdate(note.copy(description = it)) },
                    fontSize = 14.sp
                )
            }
        }
    }
}
