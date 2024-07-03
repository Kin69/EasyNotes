package com.kin.easynotes.data.local.database


import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kin.easynotes.core.constant.DatabaseConst
import com.kin.easynotes.data.local.dao.NoteDao

class NoteDatabaseProvider(private val application: Application) {

    private var database: NoteDatabase? = null

    @Synchronized
    fun instance(): NoteDatabase {
        if (database == null) {
            database = Room.databaseBuilder(application.applicationContext, NoteDatabase::class.java, DatabaseConst.NOTES_DATABASE_FILE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        return database!!
    }

    @Synchronized
    fun close() {
        database?.close()
        database = null
    }

    fun noteDao(): NoteDao {
        return instance().noteDao()
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `notes-table` ADD COLUMN `created_at` INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
    }
}
