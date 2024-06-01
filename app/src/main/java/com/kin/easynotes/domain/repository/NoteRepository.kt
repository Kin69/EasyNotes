package com.kin.easynotes.domain.repository

import com.kin.easynotes.data.dao.NoteDao
import com.kin.easynotes.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {

    fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    fun getNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    fun getNoteById(id: Int): Flow<Note> {
        return noteDao.getNoteById(id)
    }


    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun getLastNoteId(): Long? {
        return withContext(Dispatchers.IO) {
            noteDao.getLastNoteId()
        }
    }
}