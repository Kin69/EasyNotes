package com.kin.easynotes.domain.holder

import android.content.Context
import androidx.room.Room
import com.kin.easynotes.data.NoteDatabase
import com.kin.easynotes.domain.repository.NoteRepository

object DatabaseHolder {
    private lateinit var database: NoteDatabase

    val noteRepository by lazy {
        NoteRepository(noteDao = database.noteDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, NoteDatabase::class.java, "note-list.db").build()
    }
}