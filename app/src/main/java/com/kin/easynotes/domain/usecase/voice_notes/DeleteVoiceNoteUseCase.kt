package com.kin.easynotes.domain.usecase.voice_notes

import com.kin.easynotes.data.local.models.VoiceNote
import com.kin.easynotes.data.repository.VoiceNotesRepository
import javax.inject.Inject

class DeleteVoiceNoteUseCase @Inject constructor(
    private val repository: VoiceNotesRepository
) {
    suspend operator fun invoke(voiceNote: VoiceNote) {
        repository.deleteVoiceNote(voiceNote)
    }
}
