package com.kin.easynotes.data.repository

import com.kin.easynotes.data.local.dao.VoiceNoteDao
import com.kin.easynotes.data.local.models.VoiceNote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VoiceNotesRepository @Inject constructor(
    private val voiceNoteDao: VoiceNoteDao
) {

    fun getAllVoiceNotes(): Flow<List<VoiceNote>> = voiceNoteDao.getAllVoiceNotes()

    suspend fun insertVoiceNote(voiceNote: VoiceNote) {
        voiceNoteDao.insertVoiceNote(voiceNote)
    }

    suspend fun deleteVoiceNote(voiceNote: VoiceNote) {
        voiceNoteDao.deleteVoiceNote(voiceNote)
    }
}
