package com.kin.easynotes.presentation.screens.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteViewModel

class HomeViewModel : NoteViewModel() {
    val selectedNotes = mutableStateListOf<Note>()
    var editMode by mutableStateOf(false)

    fun updateEditMode(boolean: Boolean) {
        editMode =  boolean
    }

    fun toggleNoteSelection(note: Note) {
        if (selectedNotes.contains(note)) {
            selectedNotes.remove(note)
        } else {
            selectedNotes.add(note)
        }
    }

    fun deleteSelectedNotes(noteViewModel: NoteViewModel) {
        selectedNotes.forEach { note ->
            noteViewModel.deleteNoteById(note.id)
        }
        selectedNotes.clear()
        editMode = false
    }
}