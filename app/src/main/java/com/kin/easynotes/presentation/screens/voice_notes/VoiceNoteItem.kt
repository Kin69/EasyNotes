package com.kin.easynotes.presentation.screens.voice_notes

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kin.easynotes.data.local.models.VoiceNote
import com.kin.easynotes.presentation.utils.TimerUtil

@Composable
fun VoiceNoteItem(voiceNote: VoiceNote, onDelete: (VoiceNote) -> Unit) {
    val mediaPlayer = remember { MediaPlayer() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* Handle item click if needed */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = TimerUtil.formatDuration(voiceNote.duration), modifier = Modifier.weight(1f))
        IconButton(onClick = {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.apply {
                    reset()
                    setDataSource(voiceNote.filePath)
                    prepare()
                    start()
                }
            }
        }) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play/Pause")
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onDelete(voiceNote) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
