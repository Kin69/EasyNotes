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
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM `notes-table`")
    fun getAllNotes(): Flow<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM `notes-table` WHERE id=:id")
    fun getNoteById(id: Int): Flow<Note>

    @Query("SELECT id FROM `notes-table` ORDER BY id DESC LIMIT 1")
    fun getLastNoteId(): Long?
}
