package com.kin.easynotes.data.repository

import com.kin.easynotes.data.local.dao.NoteDao
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun getNoteById(id: Int): Flow<Note> {
        return noteDao.getNoteById(id)
    }

    override fun getLastNoteId(): Long? {
        return noteDao.getLastNoteId()
    }
}