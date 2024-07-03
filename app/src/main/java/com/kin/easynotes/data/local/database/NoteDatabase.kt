package com.kin.easynotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kin.easynotes.core.constant.DatabaseConst
import com.kin.easynotes.data.local.dao.NoteDao
import com.kin.easynotes.domain.model.Note

@Database(
    entities = [Note::class],
    version = DatabaseConst.NOTES_DATABASE_VERSION,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}