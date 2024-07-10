package com.kin.easynotes.domain.usecase


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.domain.model.Note
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteUseCase @Inject constructor(
    private val noteRepository: NoteRepositoryImpl,
    private val coroutineScope: CoroutineScope,
    @ApplicationContext private val context: Context
) {
    var notes: List<Note> by mutableStateOf(emptyList())
        private set

    private var observeKeysJob: Job? = null

    fun observe() {
        observeKeys()
    }

    private fun observeKeys() {
        observeKeysJob?.cancel()
        observeKeysJob = coroutineScope.launch {
            getAllNotes().collectLatest { keys ->
                this@NoteUseCase.notes = keys
//                NotesWidgetReceiver.updateBroadcast(context)
            }
        }
    }


    fun getAllNotes(): Flow<List<Note>> {
        return noteRepository.getAllNotes()
    }

    fun addNote(note: Note) {
        if (note.id == 0) {
            coroutineScope.launch(NonCancellable + Dispatchers.IO) {
                noteRepository.addNote(note)
            }
        } else {
            coroutineScope.launch(NonCancellable + Dispatchers.IO) {
                noteRepository.updateNote(note)
            }
        }
    }

    fun pinNote(note: Note) {
        coroutineScope.launch(NonCancellable + Dispatchers.IO) {
            noteRepository.updateNote(note)
        }
    }

    fun deleteNoteById(id: Int) {
        coroutineScope.launch(NonCancellable + Dispatchers.IO) {
            val noteToDelete = noteRepository.getNoteById(id).first()
            noteRepository.deleteNote(noteToDelete)
        }
    }

    fun getNoteById(id: Int): Flow<Note>  {
        return noteRepository.getNoteById(id)
    }

    fun getLastNoteId(onResult: (Long?) -> Unit) {
        coroutineScope.launch(NonCancellable + Dispatchers.IO) {
            val lastNoteId = noteRepository.getLastNoteId()
            withContext(Dispatchers.Main) {
                onResult(lastNoteId)
            }
        }
    }
}
