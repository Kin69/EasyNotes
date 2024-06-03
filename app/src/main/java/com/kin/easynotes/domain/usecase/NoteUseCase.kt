package com.kin.easynotes.domain.usecase


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class NoteViewModel (
    private val noteRepository: NoteRepository
) : ViewModel() {

    lateinit var getAllNotes: Flow<List<Note>>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllNotes = noteRepository.getAllNotes()
        }
    }

    fun addNote(note: Note) {
        if (note.id == 0) {
            viewModelScope.launch(Dispatchers.IO) {
                noteRepository.addNote(note)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                noteRepository.updateNote(note)
            }
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteToDelete = noteRepository.getNoteById(id).first()
            noteRepository.deleteNote(noteToDelete)
        }
    }

    open fun getNoteById(id: Int): Flow<Note> {
        return noteRepository.getNoteById(id)
    }

    fun getLastNoteId(onResult: (Long?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastNoteId = noteRepository.getLastNoteId()
            withContext(Dispatchers.Main) {
                onResult(lastNoteId)
            }
        }
    }
}

fun <VM: ViewModel> viewModelFactory(initializer: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return initializer() as T
        }
    }
}