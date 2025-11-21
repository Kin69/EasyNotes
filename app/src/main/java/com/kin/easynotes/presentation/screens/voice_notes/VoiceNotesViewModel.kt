package com.kin.easynotes.presentation.screens.voice_notes

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.data.local.models.VoiceNote
import com.kin.easynotes.domain.usecase.voice_notes.AddVoiceNoteUseCase
import com.kin.easynotes.domain.usecase.voice_notes.DeleteVoiceNoteUseCase
import com.kin.easynotes.domain.usecase.voice_notes.GetAllVoiceNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VoiceNotesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val addVoiceNoteUseCase: AddVoiceNoteUseCase,
    private val deleteVoiceNoteUseCase: DeleteVoiceNoteUseCase,
    private val getAllVoiceNotesUseCase: GetAllVoiceNotesUseCase
) : ViewModel() {

    private val _voiceNotes = MutableStateFlow<List<VoiceNote>>(emptyList())
    val voiceNotes = _voiceNotes.asStateFlow()

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    val voiceNotesDir = File(context.filesDir, "voice_notes")

    init {
        if (!voiceNotesDir.exists()) {
            voiceNotesDir.mkdirs()
        }
        loadVoiceNotes()
    }

    private fun loadVoiceNotes() {
        viewModelScope.launch {
            getAllVoiceNotesUseCase().collectLatest {
                _voiceNotes.value = it
            }
        }
    }

    fun startRecording() {
        val fileName = "voice_note_${System.currentTimeMillis()}.m4a"
        audioFile = File(voiceNotesDir, fileName)

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                // Handle exception
            }
        }
    }

    fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder?.pause()
        }
    }

    fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder?.resume()
        }
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        audioFile?.let { file ->
            val duration = getAudioDuration(file.absolutePath)
            val voiceNote = VoiceNote(
                filePath = file.absolutePath,
                duration = duration,
                createdAt = System.currentTimeMillis()
            )
            viewModelScope.launch {
                addVoiceNoteUseCase(voiceNote)
            }
        }
        audioFile = null
    }

    fun deleteVoiceNote(voiceNote: VoiceNote) {
        viewModelScope.launch {
            deleteVoiceNoteUseCase(voiceNote)
            File(voiceNote.filePath).delete()
        }
    }

    private fun getAudioDuration(filePath: String): Long {
        val mediaMetadataRetriever = android.media.MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(filePath)
        val durationStr = mediaMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
        return durationStr?.toLongOrNull() ?: 0L
    }
}
