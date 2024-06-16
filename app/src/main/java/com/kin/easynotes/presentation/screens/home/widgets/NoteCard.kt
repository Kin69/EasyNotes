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
    onShortClick : () -> Unit,
    onLongClick : () -> Unit,
    onNoteUpdate: (Note) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                onClick = { onShortClick() },
                onLongClick = { onLongClick() }
            )
    ) {
        Column(
            modifier = Modifier.padding(9.dp)
        ) {
            if (note.name.isNotEmpty()) {
                MarkdownText(
                    markdown = note.name.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_name_height)),
                    weight = FontWeight.Bold,
                    onContentChange = { onNoteUpdate(note.copy(name = it)) },
                    fontSize = 17.sp
                )
            }
            if (note.description.isNotEmpty()) {
                MarkdownText(
                    markdown = note.description,
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_description_height)),
                    onContentChange = { onNoteUpdate(note.copy(description = it)) },
                    fontSize = 14.sp
                )
            }
        }
    }
}
