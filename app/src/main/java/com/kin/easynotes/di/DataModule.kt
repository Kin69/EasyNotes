package com.kin.easynotes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.data.local.database.NoteDatabase
import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import com.kin.easynotes.domain.repository.NoteRepository
import com.kin.easynotes.domain.repository.SettingsRepository
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

interface DataModule {
    val noteDatabase: NoteDatabase
    val noteRepository: NoteRepository
    val settingsRepository: SettingsRepository
}

class DataModuleImpl(
    private val appContext: Context
) : DataModule {

    override val noteDatabase: NoteDatabase by lazy {
        Room.databaseBuilder(
            appContext.applicationContext,
            NoteDatabase::class.java, "note-list.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    override val noteRepository: NoteRepository by lazy {
        NoteRepositoryImpl(noteDatabase.noteDao())
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(appContext)
    }

}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `notes-table` ADD COLUMN `created_at` INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
    }
}
