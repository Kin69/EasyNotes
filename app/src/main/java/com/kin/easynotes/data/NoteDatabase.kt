package com.kin.easynotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kin.easynotes.data.dao.NoteDao
import com.kin.easynotes.domain.model.Note

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}