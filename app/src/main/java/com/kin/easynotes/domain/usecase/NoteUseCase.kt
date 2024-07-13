package com.kin.easynotes.domain.usecase


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.updateAll
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.EncryptionHelper
import com.kin.easynotes.widget.NotesWidget
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

    fun observe(encrypted: Boolean = false) {
        observeNotes(encrypted)
    }

    private fun observeNotes(encrypted: Boolean) {
        observeKeysJob?.cancel()
        observeKeysJob = coroutineScope.launch {
            if (!encrypted) {
                getAllNotes().collectLatest { notes ->
                    this@NoteUseCase.notes = notes
                    NotesWidget().updateAll(context)
                }
            } else {
                getAllEncryptedNotes().collectLatest { encryptedNotes ->
                    val decryptedNotes = encryptedNotes.map { note ->
                        val encryptionHelper = EncryptionHelper(context)
                        val name = encryptionHelper.decrypt(note.name)
                        val description = encryptionHelper.decrypt(note.description)
                        note.copy(name = name, description = description)
                    }
                    this@NoteUseCase.notes = decryptedNotes
                }
            }
        }
    }

    /* Return only encrypted notes */
    private fun getAllEncryptedNotes(): Flow<List<Note>> {
        return noteRepository.getAllEncryptedNotes()
    }

    private fun getAllNotes(): Flow<List<Note>> {
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
