package com.kin.easynotes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kin.easynotes.data.local.models.VoiceNote
import kotlinx.coroutines.flow.Flow

@Dao
interface VoiceNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoiceNote(voiceNote: VoiceNote)

    @Query("SELECT * FROM voice_notes ORDER BY createdAt DESC")
    fun getAllVoiceNotes(): Flow<List<VoiceNote>>

    @Delete
    suspend fun deleteVoiceNote(voiceNote: VoiceNote)
}
