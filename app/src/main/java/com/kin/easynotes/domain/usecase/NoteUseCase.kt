package com.kin.easynotes.domain.usecase

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.updateAll
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.DecryptionResult
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
    private val encryptionHelper: EncryptionHelper,
    @ApplicationContext private val context: Context
) {
    var notes: List<Note> by mutableStateOf(emptyList())
        private set

    var decryptionResult: DecryptionResult by mutableStateOf(DecryptionResult.LOADING)

    private var observeKeysJob: Job? = null

    fun observe() {
        observeNotes()
    }

    private fun observeNotes() {
        observeKeysJob?.cancel()
        observeKeysJob = coroutineScope.launch {
            getAllNotes().collectLatest { notes ->
                val hasUnencryptedNotes = notes.any { !it.encrypted }
                if (!hasUnencryptedNotes) this@NoteUseCase.decryptionResult = DecryptionResult.EMPTY
                val processedNotes = notes.mapNotNull { note ->
                    if (note.encrypted) {
                        val (decryptedNote, status) = decryptNote(note)
                        this@NoteUseCase.decryptionResult = status
                        if (status == DecryptionResult.SUCCESS) decryptedNote else null
                    } else {
                        note
                    }
                }
                this@NoteUseCase.notes = processedNotes
                NotesWidget().updateAll(context)
            }
        }
    }

    private fun encryptNote(note: Note): Note {
        return if (note.encrypted) {
            note.copy(
                name = encryptionHelper.encrypt(note.name),
                description = encryptionHelper.encrypt(note.description),
                encrypted = true
            )
        } else {
            note
        }
    }

    private fun decryptNote(note: Note): Pair<Note,DecryptionResult> {
        val (decryptedName, nameResult) = encryptionHelper.decrypt(note.name)
        val (decryptedDescription, descriptionResult) = encryptionHelper.decrypt(note.description)
        return if (note.encrypted) {
            Pair(note.copy(
                name = decryptedName ?: "",
                description = decryptedDescription ?: "",
            ), descriptionResult)
        } else {
            Pair(note, DecryptionResult.SUCCESS)
        }
    }

    private fun getAllNotes(): Flow<List<Note>> {
        return noteRepository.getAllNotes()
    }

    suspend fun addNote(note: Note) {
        val noteToSave = encryptNote(note)
        if (note.id == 0) {
            noteRepository.addNote(noteToSave)
        } else {
            noteRepository.updateNote(noteToSave)
        }
    }

    fun pinNote(note: Note) {
        coroutineScope.launch(NonCancellable + Dispatchers.IO) {
            addNote(note)
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
