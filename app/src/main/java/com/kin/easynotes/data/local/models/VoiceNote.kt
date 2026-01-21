package com.kin.easynotes.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voice_notes")
data class VoiceNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filePath: String,
    val duration: Long,
    val createdAt: Long
)
