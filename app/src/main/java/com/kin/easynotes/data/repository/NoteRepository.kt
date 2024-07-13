package com.kin.easynotes.data.repository

import com.kin.easynotes.data.local.dao.NoteDao
import com.kin.easynotes.data.local.database.NoteDatabaseProvider
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val provider: NoteDatabaseProvider) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        return provider.noteDao().getAllNotes()
    }

    override fun getAllEncryptedNotes(): Flow<List<Note>> {
        return provider.noteDao().getAllEncryptedNotes()
    }

    override suspend fun addNote(note: Note) {
        provider.noteDao().addNote(note)
    }

    override suspend fun updateNote(note: Note) {
        provider.noteDao().updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        provider.noteDao().deleteNote(note)
    }

    override fun getNoteById(id: Int): Flow<Note> {
        return provider.noteDao().getNoteById(id)
    }

    override fun getLastNoteId(): Long? {
        return provider.noteDao().getLastNoteId()
    }
}