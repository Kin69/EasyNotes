package com.kin.easynotes.domain.usecase


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteUseCase (
    private val noteRepository: NoteRepository,
    private val coroutineScope: CoroutineScope
) {

    lateinit var getAllNotes: Flow<List<Note>>

    init {
        coroutineScope.launch(Dispatchers.IO) {
            getAllNotes = noteRepository.getAllNotes()
        }
    }

    fun addNote(note: Note) {
        if (note.id == 0) {
            coroutineScope.launch(Dispatchers.IO) {
                noteRepository.addNote(note)
            }
        } else {
            coroutineScope.launch(Dispatchers.IO) {
                noteRepository.updateNote(note)
            }
        }
    }

    fun deleteNoteById(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            val noteToDelete = noteRepository.getNoteById(id).first()
            noteRepository.deleteNote(noteToDelete)
        }
    }

    fun getNoteById(id: Int): Flow<Note> {
        return noteRepository.getNoteById(id)
    }

    fun getLastNoteId(onResult: (Long?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val lastNoteId = noteRepository.getLastNoteId()
            withContext(Dispatchers.Main) {
                onResult(lastNoteId)
            }
        }
    }
}
