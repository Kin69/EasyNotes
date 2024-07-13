package com.kin.easynotes.domain.repository

import com.kin.easynotes.domain.model.Note
import dagger.Provides
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllEncryptedNotes(): Flow<List<Note>>
    fun getAllNotes(): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNoteById(id: Int): Flow<Note>
    fun getLastNoteId(): Long?
}