package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Note

@Composable
fun NoteFilter(
    onNoteClicked: (Int) -> Unit,
    notes: List<Note>,
    searchText: String? = null,
    selectedNotes: MutableList<Int>,
    isDeleteMode: Boolean,
    onNoteUpdate: (Note) -> Unit,
    onDeleteNote: (Int) -> Unit
) {
    val filteredNotes = filterNotes(notes, searchText)
    if (filteredNotes.isEmpty()) {
        EmptyNoteList(getEmptyText(searchText))
    } else {
        NotesGrid(
            onNoteClicked,
            notes = filteredNotes,
            onNoteUpdate = onNoteUpdate,
            selectedNotes = selectedNotes,
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
