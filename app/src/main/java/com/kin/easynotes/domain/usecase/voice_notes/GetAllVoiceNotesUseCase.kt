package com.kin.easynotes.domain.usecase.voice_notes

import com.kin.easynotes.data.local.models.VoiceNote
import com.kin.easynotes.data.repository.VoiceNotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllVoiceNotesUseCase @Inject constructor(
    private val repository: VoiceNotesRepository
) {
    operator fun invoke(): Flow<List<VoiceNote>> {
        return repository.getAllVoiceNotes()
    }
}
