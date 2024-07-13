package com.kin.easynotes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kin.easynotes.domain.model.Note

import kotlinx.coroutines.flow.Flow

@Dao
abstract class NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun addNote(note: Note)

    @Query("SELECT * FROM `notes-table` WHERE encrypted = 0")
    abstract fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM `notes-table` WHERE encrypted = 1")
    abstract fun getAllEncryptedNotes(): Flow<List<Note>>

    @Update
    abstract suspend fun updateNote(note: Note)

    @Delete
    abstract suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM `notes-table` WHERE id=:id")
    abstract fun getNoteById(id: Int): Flow<Note>

    @Query("SELECT id FROM `notes-table` ORDER BY id DESC LIMIT 1")
    abstract fun getLastNoteId(): Long?
}