package com.kin.easynotes.di

import android.content.Context
import androidx.room.Room
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.data.local.database.NoteDatabase
import com.kin.easynotes.data.settings.Preferences
import com.kin.easynotes.domain.repository.NoteRepository

interface DataModule {
    val noteDatabase: NoteDatabase
    val noteRepository: NoteRepository
    val preferences: Preferences
}

class DataModuleImpl(
    private val appContext: Context
) : DataModule {
    override val noteDatabase: NoteDatabase by lazy {
        Room.databaseBuilder(
            appContext.applicationContext,
            NoteDatabase::class.java, "note_database"
        ).build()
    }

    override val noteRepository: NoteRepository by lazy {
        NoteRepositoryImpl(noteDatabase.noteDao())
    }

    override val preferences: Preferences by lazy {
        Preferences(appContext)
    }
}