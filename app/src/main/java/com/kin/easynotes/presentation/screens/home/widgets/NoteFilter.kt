package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Note

@Composable
fun NoteFilter(
    containerColor : Color,
    onNoteClicked: (Int) -> Unit,
    shape: RoundedCornerShape,
    notes: List<Note>,
    searchText: String? = null,
    selectedNotes: MutableList<Int>,
    viewMode: Boolean = false,
    isDeleteMode: Boolean,
    onNoteUpdate: (Note) -> Unit,
    onDeleteNote: (Int) -> Unit
) {
    val filteredNotes = filterNotes(notes, searchText)
    if (filteredNotes.isEmpty()) {
        Placeholder(
            placeholderIcon = {
                Icon(
                    getEmptyIcon(searchText),
                    contentDescription = "Placeholder icon",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(64.dp)
                )
            },
            placeholderText = getEmptyText(searchText)
        )
    } else {
        NotesGrid(
            containerColor = containerColor,
            onNoteClicked = onNoteClicked,
            notes = filteredNotes,
            shape = shape,
            onNoteUpdate = onNoteUpdate,
            selectedNotes = selectedNotes,
            viewMode = viewMode,
            isDeleteClicked = isDeleteMode,
            animationFinished = onDeleteNote
        )
    }
}

private fun filterNotes(notes: List<Note>, searchText: String?): List<Note> {
    return searchText?.let { query ->
        if (query.isBlank()) {
            emptyList()
        } else {
            notes.filter { note ->
                note.name.contains(query, ignoreCase = true) || note.description.contains(query, ignoreCase = true)
            }
        }
    } ?: notes
}

@Composable
private fun getEmptyText(searchText: String?): String {
    return when (searchText) {
        null -> stringResource(R.string.no_created_notes)
        "" -> stringResource(R.string.no_found_notes)
        else -> stringResource(R.string.no_found_notes)
    }
}

@Composable
private fun getEmptyIcon(searchText: String?): ImageVector {
    return when (searchText) {
        null -> Icons.Rounded.Notes
        "" -> Icons.Rounded.Search
        else -> Icons.Rounded.Search
    }
}

