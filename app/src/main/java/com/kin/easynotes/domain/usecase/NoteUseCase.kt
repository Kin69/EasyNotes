package com.kin.easynotes.domain.usecase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.domain.holder.DatabaseHolder
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class NoteViewModel(
    private val noteRepository: NoteRepository = DatabaseHolder.noteRepository
) : ViewModel() {

    lateinit var getAllNotes: Flow<List<Note>>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllNotes = noteRepository.getNotes()
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(note)
        }
    }

    fun getNoteById(id: Int): Flow<Note> {
        return noteRepository.getNoteById(id)
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteToDelete = noteRepository.getNoteById(id).first()
            noteRepository.deleteNote(noteToDelete)
        }
    }
}